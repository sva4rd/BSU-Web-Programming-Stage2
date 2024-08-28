package controller;

import connection.ConnectionPoolException;
import jakarta.servlet.http.HttpSession;
import model.Bill;
import model.Car;
import model.Client;
import modelDAO.CarDAO;
import modelDAO.DAOException;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;
import java.util.List;

public class RentCarController implements IController{

    public RentCarController(){}

    @Override
    public void setList(WebContext ctx){
        List<Car> cars = null;
        try{
            cars = CarDAO.getAllAvailableCars();
        } catch (Exception ignored) {}
        ctx.setVariable("carList", cars);
    }

    @Override
    public void processWrongInput(final IWebExchange webExchange, final ITemplateEngine templateEngine,
                                  final Writer writer){
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        setList(ctx);
        ctx.setVariable("billId", "-");
        ctx.setVariable("requestResult", "Error: wrong input");
        ctx.setVariable("billResult", "");
        ctx.setVariable("billButtonDisabled", true);
        templateEngine.process("rent_car", ctx, writer);
    }

    @Override
    public void processList(HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine,
                               Writer writer) {
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        setList(ctx);
        ctx.setVariable("billId", "-");
        session.setAttribute("billId", "-");
        ctx.setVariable("requestResult", "");
        ctx.setVariable("billResult", "");
        ctx.setVariable("billButtonDisabled", true);
        templateEngine.process("rent_car", ctx, writer);
    }

    @Override
    public void processID (HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine,
                           Writer writer, int carID) throws DAOException, ConnectionPoolException {
        if (carID == -1) {
            processBillID(session, webExchange, templateEngine, writer);
        } else {
            processCarID(session, webExchange, templateEngine, writer, carID);
        }
    }

    private void processCarID (HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine,
                               Writer writer, int carID) throws DAOException, ConnectionPoolException {
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        Client client = new Client("MP4091234", false);
        int billID = 0;
        try {
            billID = Admin.processRequest(client.createRequest(CarDAO.getCarById(carID), 10));
        } catch (Exception ex){
            setList(ctx);
            ctx.setVariable("requestResult", "Request creation failed");
            ctx.setVariable("billId", "-");
            session.setAttribute("billId", "-");
            ctx.setVariable("billResult", "");
            ctx.setVariable("billButtonDisabled", true);
            templateEngine.process("rent_car", ctx, writer);
            return;
        }
        if (billID != 0 ) {
            ctx.setVariable("billId", billID);
            session.setAttribute("billId", billID);
            ctx.setVariable("billButtonDisabled", false);
        } else {
            ctx.setVariable("billId", "-");
            session.setAttribute("billId", "-");
            ctx.setVariable("billButtonDisabled", true);
        }

        setList(ctx);
        ctx.setVariable("requestResult", "Request created successfully");
        ctx.setVariable("billResult", "");
        templateEngine.process("rent_car", ctx, writer);
    }

    private void processBillID (HttpSession session, IWebExchange webExchange,
                                ITemplateEngine templateEngine, Writer writer) {
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        ctx.setVariable("billButtonDisabled", true);
        try {
            Client client = new Client("MP4091234", false);
            Integer billID = (Integer) session.getAttribute("billId");
            ctx.setVariable("billId", "-");
            session.setAttribute("billId", "-");
            client.payBill(billID);
        } catch (Exception ignored){
            setList(ctx);
            ctx.setVariable("requestResult", "");
            ctx.setVariable("billResult", "Bill payment failed");
            templateEngine.process("rent_car", ctx, writer);
            return;
        }
        setList(ctx);
        ctx.setVariable("requestResult", "");
        ctx.setVariable("billResult", "Bill paid successfully");
        templateEngine.process("rent_car", ctx, writer);
    }
}
