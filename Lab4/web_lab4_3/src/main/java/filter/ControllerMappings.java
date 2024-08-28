package filter;

import java.util.HashMap;
import java.util.Map;

import controller.*;
import org.thymeleaf.web.IWebRequest;

public class ControllerMappings {
    private static final Map<String, IController> controllersByURL;

    static {
        controllersByURL = new HashMap<>();
        controllersByURL.put("/", new HomeController());
        controllersByURL.put("/register", new RegisterController());
        controllersByURL.put("/rent_car", new RentCarController());
        controllersByURL.put("/rental_car_list", new RentalCarsListController());
    }

    public static IController resolveControllerForRequest(final IWebRequest request) {
        final String path = request.getPathWithinApplication();
        return controllersByURL.get(path);
    }
}
