package controller;

import connection.ConnectionPoolException;
import jakarta.servlet.http.HttpSession;
import model.Car;
import model.Client;
import modelDAO.CarDAO;
import modelDAO.DAOException;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;
import java.util.List;

public class RentalCarsListController implements IController{
    public RentalCarsListController() {}

    public void setList(WebContext ctx, String passportData){
        List<Car> cars = null;
        try {
            cars = CarDAO.getCarsForClient(passportData);
        } catch (Exception ignored) {}
        ctx.setVariable("clientCarList", cars);
        if (cars == null || cars.isEmpty())
            ctx.setVariable("emptyClientCarList", true);
        else
            ctx.setVariable("emptyClientCarList", false);
    }

    @Override
    public void processWrongInput(HttpSession session, final IWebExchange webExchange, final ITemplateEngine templateEngine,
                                  final Writer writer){
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        String passportData = (String) session.getAttribute("client");
        setList(ctx, passportData);
        ctx.setVariable("releaseResult", "Error: wrong input");
        templateEngine.process("rental_car_list", ctx, writer);
    }

    @Override
    public void processList(HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine,
                               Writer writer) {
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        String client = (String) session.getAttribute("client");
        setList(ctx, client);
        ctx.setVariable("releaseResult", "");
        templateEngine.process("rental_car_list", ctx, writer);
    }

    @Override
    public void processID(HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine,
                          Writer writer, String carID) throws DAOException, ConnectionPoolException {
        int ID = Integer.parseInt(carID);
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        String passportData = (String) session.getAttribute("client");
        Client client = new Client(passportData, false);
        if (client.releaseCar(ID)) {
            setList(ctx, passportData);
            ctx.setVariable("releaseResult", "Car successfully released");
            templateEngine.process("rental_car_list", ctx, writer);
        } else {
            setList(ctx, passportData);
            ctx.setVariable("releaseResult", "Car release failed");
            templateEngine.process("rental_car_list", ctx, writer);
        }
    }
}
