package com.app.weblab5.controller;

import com.app.weblab5.model.Client;
import com.app.weblab5.model.User;
import com.app.weblab5.repository.ClientRepository;
import com.app.weblab5.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RegisterController {
    @Autowired
    private ClientRepository clientRep;
    @Autowired
    private UserRepository userRep;

    @GetMapping(value="/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping(value = "/register_proc")
    @Transactional
    public String processRegistration(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      @RequestParam("passportInput") String passportInput,
                                      Model model){
        if (username == null || password == null || passportInput == null ||
        username.isEmpty() || password.isEmpty() || passportInput.isEmpty()){
            model.addAttribute("signUpResult", "Error: fields shouldn't be empty");
            return "register";
        }
        int id = 0;
        User user;
        try {
            user = User.createUser(username, password);
            String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encodedPassword);
            if (userRep.findByUsername(username) != null)
                throw new Exception();
        } catch (Exception e) {
            model.addAttribute("signUpResult", "Error: wrong username");
            return "register";
        }

        try {
            List<Client> inUse = clientRep.getClientById(passportInput);
            if (!inUse.isEmpty())
                throw new Exception();
        } catch (Exception e) {
            model.addAttribute("signUpResult", "Error: passport data already in use");
            return "register";
        }

        userRep.save(user);
        id = user.getId();
        Client client = new Client(passportInput, id);
        clientRep.save(client);
        return "redirect:/login";
    }
}
