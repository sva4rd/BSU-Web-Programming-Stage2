package filter;

import controller.IController;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
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

@WebFilter(urlPatterns = "/*")
public class AppFilter implements Filter {
    private JakartaServletWebApplication application;
    private ITemplateEngine templateEngine;

    @Override
    public void init(FilterConfig filterConfig) {
        this.application = JakartaServletWebApplication.buildApplication(filterConfig.getServletContext());
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
            controller.processWrongInput(session, webExchange, templateEngine, writer);
        } else{
            controller.processID(session, webExchange, templateEngine, writer, ID);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (!process(req, res)) {
            chain.doFilter(request, response);
        }
    }

    public boolean process( HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            final IServletWebExchange webExchange = this.application.buildExchange(req, res);
            final IWebRequest webRequest = webExchange.getRequest();
            final Writer writer = res.getWriter();

            if (webRequest.getPathWithinApplication().startsWith("/css") ||
                    webRequest.getPathWithinApplication().startsWith("/images") ||
                    webRequest.getPathWithinApplication().startsWith("/favicon")) {
                return false;
            }

            HttpSession session = req.getSession();

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
            res.addCookie(lastVisitCookie);
            res.addCookie(visitCountCookie);

            res.setContentType("text/html;charset=UTF-8");
            res.setHeader("Pragma", "no-cache");
            res.setHeader("Cache-Control", "no-cache");
            res.setDateHeader("Expires", 0);

            IController controller = ControllerMappings.resolveControllerForRequest(webRequest);
            String carData = req.getParameter("carIdInput");
            String billData = req.getParameter("payBillAction");
            String relCarId = req.getParameter("relCarId");
            String passportData = req.getParameter("passportInput");
            String logOutButton = req.getParameter("logOutButton");
            if (carData != null) {
                checkIdProcess(session, webExchange, templateEngine, writer, controller, carData);
            } else if (billData != null) {
                controller.processID(session, webExchange, templateEngine, writer, "-1");
            } else if (relCarId != null) {
                checkIdProcess(session, webExchange, templateEngine, writer, controller, relCarId);
            } else if (logOutButton != null){
                session.removeAttribute("client");
                res.sendRedirect(req.getContextPath() + "/");
            } else if (passportData != null){
                if (Objects.equals(passportData, "") || passportData.length() > 255)
                    controller.processWrongInput(session, webExchange, templateEngine, writer);
                else
                    controller.processID(session, webExchange, templateEngine, writer, passportData);
            } else if (controller != null &&  controller.getClass().getSimpleName().equals("RentalCarsListController")){
                String client = (String) session.getAttribute("client");
                if (client == null || client.isEmpty())
                    res.sendRedirect(req.getContextPath() + "/register");
                else
                    controller.processList(session, webExchange, templateEngine, writer);
            } else if (controller != null){
                controller.processList(session, webExchange, templateEngine, writer);
            } else {
                return false;
            }

            return true;

        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException(e);
        }
    }
}