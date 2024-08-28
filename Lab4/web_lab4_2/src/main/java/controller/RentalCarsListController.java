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

    @Override
    public void setList(WebContext ctx){
        List<Car> cars = null;
        try {
            cars = CarDAO.getCarsForClient("MP4091234");
        } catch (Exception ignored) {}
        ctx.setVariable("clientCarList", cars);
    }

    @Override
    public void processWrongInput(final IWebExchange webExchange, final ITemplateEngine templateEngine,
                                  final Writer writer){
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        setList(ctx);
        ctx.setVariable("releaseResult", "Error: wrong input");
        templateEngine.process("rental_car_list", ctx, writer);
    }

    @Override
    public void processList(HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine,
                               Writer writer) {
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        setList(ctx);
        ctx.setVariable("releaseResult", "");
        templateEngine.process("rental_car_list", ctx, writer);
    }

    @Override
    public void processID(HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine,
                          Writer writer, int ID) throws DAOException, ConnectionPoolException {
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        Client client = new Client("MP4091234", false);
        if (client.releaseCar(ID)) {
            setList(ctx);
            ctx.setVariable("releaseResult", "Car successfully released");
            templateEngine.process("rental_car_list", ctx, writer);
        } else {
            setList(ctx);
            ctx.setVariable("releaseResult", "Car release failed");
            templateEngine.process("rental_car_list", ctx, writer);
        }
    }
}
