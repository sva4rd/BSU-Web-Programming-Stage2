package controller;

import java.io.Writer;

import jakarta.servlet.http.HttpSession;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;

public interface IController {

    void processWrongInput(HttpSession session, final IWebExchange webExchange, final ITemplateEngine templateEngine,
                           final Writer writer);

    void processList(HttpSession session, final IWebExchange webExchange, final ITemplateEngine templateEngine,
                     final Writer writer) throws Exception;
    void processID(HttpSession session, final IWebExchange webExchange, final ITemplateEngine templateEngine,
                   final Writer writer, String ID) throws Exception;
}
