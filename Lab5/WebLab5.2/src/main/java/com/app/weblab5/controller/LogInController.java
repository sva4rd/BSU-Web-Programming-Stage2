package com.app.weblab5.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LogInController {

    @GetMapping(value="/login", params = "error")
    public String showRegister(@RequestParam(value = "error", required = false) String error,
                               Model model) {
        if (error != null) {
            model.addAttribute("signInResult", "Error: wrong username or password");
        }
        return "login";
    }

    @GetMapping(value="/login")
    public String showRegister(HttpSession session, Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        return "login";
    }
}

//@Controller
//public class LogInController {
//
//    @GetMapping(value="/login", params = "error", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public Map<String, String> showRegisterWithError(@RequestParam(value = "error", required = false) String error) {
//        Map<String, String> response = new HashMap<>();
//        if (error != null) {
//            response.put("signInResult", "Error: wrong username or password");
//        }
//        return response;
//    }
//
//    @GetMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public Map<String, Object> showRegister(HttpSession session) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("username", session.getAttribute("username"));
//        return response;
//    }
//}
