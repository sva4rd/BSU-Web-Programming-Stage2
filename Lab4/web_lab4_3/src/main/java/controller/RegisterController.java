package controller;

import connection.ConnectionPoolException;
import jakarta.servlet.http.HttpSession;
import model.Car;
import model.Client;
import modelDAO.CarDAO;
import modelDAO.ClientDAO;
import modelDAO.DAOException;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;
import java.util.List;

public class RegisterController implements IController {
    public RegisterController(){}

    public void setList(WebContext ctx){}

    @Override
    public void processWrongInput(HttpSession session, final IWebExchange webExchange, final ITemplateEngine templateEngine,
                                  final Writer writer){
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        ctx.setVariable("passportData", "");
        ctx.setVariable("signInResult", "Error: wrong input");
        String passportData = (String) session.getAttribute("client");
        ctx.setVariable("notAuthorized", passportData == null);
        templateEngine.process("register", ctx, writer);
    }

    @Override
    public void processList(HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine,
                            Writer writer) {
        String passportData = (String) session.getAttribute("client");
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        if (passportData == null || passportData.isEmpty()){
            ctx.setVariable("passportData", "");
            ctx.setVariable("signInResult", "Please, sign in to access");
            ctx.setVariable("notAuthorized", true);
        } else {
            ctx.setVariable("passportData", passportData);
            ctx.setVariable("signInResult", "");
            ctx.setVariable("notAuthorized", false);
        }
        templateEngine.process("register", ctx, writer);
    }

    @Override
    public void processID (HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine,
                           Writer writer, String passportData) throws DAOException, ConnectionPoolException {
        try{
            Client client = ClientDAO.getClientById(passportData);
        } catch (Exception ex){
            Client client = new Client(passportData, true);
        }
        session.setAttribute("client", passportData);
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        ctx.setVariable("signInResult", "");
        templateEngine.process("home", ctx, writer);
    }
}
