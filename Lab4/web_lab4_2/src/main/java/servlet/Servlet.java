package servlet;

import controller.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.IWebRequest;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.Writer;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@WebServlet(urlPatterns = "/serv/*", loadOnStartup = 1)
public class Servlet extends HttpServlet {
    private JakartaServletWebApplication application;
    private ITemplateEngine templateEngine;

    @Override
    public void init() {
        this.application = JakartaServletWebApplication.buildApplication(getServletContext());
        this.templateEngine = buildTemplateEngine(this.application);
    }

    private ITemplateEngine buildTemplateEngine(final IWebApplication application) {
        final WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);

        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCacheable(true);

        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }

    private void checkIdProcess(HttpSession session, IWebExchange webExchange, ITemplateEngine templateEngine,
                                Writer writer, IController controller, String ID) throws Exception {
        if (Objects.equals(ID, "") || Integer.parseInt(ID) <= 0){
            controller.processWrongInput(webExchange, templateEngine, writer);
        } else{
            controller.processID(session, webExchange, templateEngine, writer, Integer.parseInt(ID));
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            final IServletWebExchange webExchange = this.application.buildExchange(request, response);
            final IWebRequest webRequest = webExchange.getRequest();
            final Writer writer = response.getWriter();

            HttpSession session = request.getSession();

            Integer visitCount = (Integer) session.getAttribute("visitCount");
            if (visitCount == null) {
                visitCount = 1;
            } else {
                visitCount++;
            }
            session.setAttribute("visitCount", visitCount);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = dateFormat.format(new Date());

            String encodedCurrentTime = URLEncoder.encode(currentTime, StandardCharsets.UTF_8);
            String encodedVisitCount = URLEncoder.encode(String.valueOf(visitCount), StandardCharsets.UTF_8);

            Cookie lastVisitCookie = new Cookie("lastVisit", encodedCurrentTime);
            Cookie visitCountCookie = new Cookie("visitCount", encodedVisitCount);
            response.addCookie(lastVisitCookie);
            response.addCookie(visitCountCookie);

            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            IController controller = ControllerMappings.resolveControllerForRequest(webRequest);
            String carData = request.getParameter("carIdInput");
            String billData = request.getParameter("payBillAction");
            String relCarId = request.getParameter("relCarId");
            if (carData != null) {
                checkIdProcess(session, webExchange, templateEngine, writer, controller, carData);
            } else if (billData != null) {
                controller.processID(session, webExchange, templateEngine, writer, -1);
            } else if (relCarId != null) {
                checkIdProcess(session, webExchange, templateEngine, writer, controller, relCarId);
            } else {
                controller.processList(session, webExchange, templateEngine, writer);
            }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException(e);
        }
    }
}