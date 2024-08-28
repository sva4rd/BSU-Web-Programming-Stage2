package com.app.weblab5.controller;
import com.app.weblab5.model.Car;
import com.app.weblab5.model.Client;
import com.app.weblab5.model.User;
import com.app.weblab5.repository.*;

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
public class RentCarController {
    @Autowired
    private CarRepository carRep;
    @Autowired
    private RequestRepository requestRep;
    @Autowired
    private OrderRepository orderRep;
    @Autowired
    private BillRepository billRep;
    @Autowired
    private ClientRepository clientRep;
    @Autowired
    private UserRepository userRep;

    private void setListAndAuthorization(HttpSession session, Model model){
        List<Car> list = carRep.getAllAvailableCars();
        model.addAttribute("carList", list);
        if (list == null || list.isEmpty())
            model.addAttribute("emptyAvailableCarList", true);
        else
            model.addAttribute("emptyAvailableCarList", false);

        String username = (String) session.getAttribute("username");
        if (username != null && !username.isEmpty())
            model.addAttribute("notAuthorized", false);
        else {
            model.addAttribute("notAuthorized", true);
            model.addAttribute("requestResult", "Please sign in to access");
        }
    }

    private void billButtonAccess(HttpSession session, Model model){
        String billButtonDisabled = (String) session.getAttribute("billButtonDisabled");
        if (billButtonDisabled == null || billButtonDisabled.equals("yes"))
            model.addAttribute("billButtonDisabled", true);
        else
            model.addAttribute("billButtonDisabled", false);
    }

    private void setBillID(HttpSession session, Model model){
        String billID = (String) session.getAttribute("billId");
        if (billID == null || billID.equals("-"))
            model.addAttribute("billId", "-");
        else
            model.addAttribute("billId", billID);
    }

    private void setResults(HttpSession session, Model model) {
        if (session.getAttribute("requestFailed") != null) {
            boolean requestFailed = false;
            requestFailed = (boolean) session.getAttribute("requestFailed");
            if (requestFailed)
                model.addAttribute("requestResult", "Request creation failed");
            else
                model.addAttribute("requestResult", "Request created successfully");
            session.removeAttribute("requestFailed");
        }

        if (session.getAttribute("payFailed") != null) {
            boolean payFailed = false;
            payFailed = (boolean) session.getAttribute("payFailed");
            if (payFailed)
                model.addAttribute("billResult", "Bill payment failed");
            else
                model.addAttribute("billResult", "Bill paid successfully");
            session.removeAttribute("payFailed");
        }
    }

    @GetMapping(value="/rent_car")
    public String getList(HttpSession session, Model model) {
        setListAndAuthorization(session, model);
        billButtonAccess(session, model);
        setBillID(session, model);
        setResults(session, model);
        return "rent_car";
    }

    @PostMapping(value="/request_proc")
    public String processRequest(@RequestParam("carIdInput") int carId,
                                 @RequestParam("rentDaysInput") int rent_days,
                                 HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userRep.findByUsername(username);
        Client client = clientRep.getClientByUserID(user.getId()).get(0);

        int requestID, orderID, billID;
        try {
            Car car = carRep.getCarById(carId).get(0);
            if (!car.checkAvailable())
                throw new Exception();
            if (rent_days <= 0 || rent_days > 30)
                throw new Exception();
            requestRep.addRequest(rent_days, carId, client.getPassportData());
            requestID = requestRep.getLastInsertedId();
            orderRep.addOrder(requestID);
            orderID = orderRep.getLastInsertedId();
            billRep.addBill(false, client.getPassportData(), orderID);
            billID = billRep.getLastInsertedId();

            session.setAttribute("billId",Integer.toString(billID));
            session.setAttribute("requestFailed", false);
            session.setAttribute("billButtonDisabled", "no");
        } catch (Exception ex){
            session.setAttribute("billId", "-");
            session.setAttribute("requestFailed", true);
            session.setAttribute("billButtonDisabled", "yes");
        }
        return "redirect:/rent_car";
    }

    @PostMapping(value="/bill_proc")
    @Transactional
    public String payBill(HttpSession session) {
        int billID = Integer.parseInt((String) session.getAttribute("billId"));
        try{
            clientRep.payBill(billID);
            session.setAttribute("payFailed", false);
        } catch (Exception ex){
            session.setAttribute("payFailed", true);
        }

        session.setAttribute("billId", "-");
        session.setAttribute("billButtonDisabled", "yes");
        return "redirect:/rent_car";
    }
}
