package com.app.weblab5.controller;

import com.app.weblab5.model.Car;
import com.app.weblab5.model.Client;
import com.app.weblab5.model.User;
import com.app.weblab5.repository.CarRepository;
import com.app.weblab5.repository.ClientRepository;
import com.app.weblab5.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RentalCarListController {
    @Autowired
    private ClientRepository clientRep;
    @Autowired
    private CarRepository carRep;
    @Autowired
    private UserRepository userRep;

    private void setList(HttpSession session, Model model, String passportData){
        List<Car> list = carRep.getCarsForClient(passportData);
        model.addAttribute("clientCarList", list);
        if (list == null || list.isEmpty())
            model.addAttribute("emptyClientCarList", true);
        else
            model.addAttribute("emptyClientCarList", false);
    }

    private void setResults(HttpSession session, Model model) {
        if (session.getAttribute("releaseFailed") != null) {
            boolean releaseFailed = false;
            releaseFailed = (boolean) session.getAttribute("releaseFailed");
            if (releaseFailed)
                model.addAttribute("releaseResult", "Car release failed");
            else
                model.addAttribute("releaseResult", "Car successfully released");
            session.removeAttribute("releaseFailed");
        }

        if (session.getAttribute("wrongInput") != null) {
            boolean wrongInput = false;
            wrongInput = (boolean) session.getAttribute("wrongInput");
            if (wrongInput)
                model.addAttribute("releaseResult", "Error: wrong input");
            session.removeAttribute("wrongInput");
        }
    }

    @GetMapping(value="/rental_car_list")
    @Transactional
    public String getList(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        User user = userRep.findByUsername(username);
        Client client = clientRep.getClientByUserID(user.getId()).get(0);
        setList(session, model, client.getPassportData());
        setResults(session, model);
        return "rental_car_list";
    }

    @PostMapping(value="/release_proc")
    @Transactional
    public String releaseCar(@RequestParam(value = "relCarId", required = false) Integer carId,
                             HttpSession session) {
        if (carId == null)
            session.setAttribute("wrongInput", true);
        else {
            String username = (String) session.getAttribute("username");
            User user = userRep.findByUsername(username);
            Client client = clientRep.getClientByUserID(user.getId()).get(0);
            String passportData = client.getPassportData();
            try {
                List<Car> cars = carRep.getCarsForClient(passportData);
                boolean success = false;
                for (Car c : cars) {
                    if (c.getID() == carId) {
                        carRep.releaseCarById(carId);
                        success = true;
                        break;
                    }
                }
                if (!success)
                    throw new Exception();
                session.setAttribute("releaseFailed", false);
            } catch (Exception ex) {
                session.setAttribute("releaseFailed", true);
            }
        }
        return "redirect:/rental_car_list";
    }
}
