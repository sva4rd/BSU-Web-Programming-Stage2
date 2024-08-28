package controller;

import jakarta.servlet.http.HttpSession;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;

public class HomeController implements IController {

    @Override
    public void processWrongInput(HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) {

    }

    @Override
    public void processList(HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) throws Exception {
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        templateEngine.process("home", ctx, writer);
    }

    @Override
    public void processID(HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer, String ID) throws Exception {

    }
}