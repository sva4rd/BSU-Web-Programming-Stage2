package servlet;

import java.util.HashMap;
import java.util.Map;

import controller.IController;
import controller.RentCarController;
import controller.RentalCarsListController;
import org.thymeleaf.web.IWebRequest;

public class ControllerMappings {
    private static final Map<String, IController> controllersByURL;

    static {
        controllersByURL = new HashMap<>();
        controllersByURL.put("/serv/rent_car", new RentCarController());
        controllersByURL.put("/serv/rental_car_list", new RentalCarsListController());
    }

    public static IController resolveControllerForRequest(final IWebRequest request) {
        final String path = request.getPathWithinApplication();
        return controllersByURL.get(path);
    }
}
