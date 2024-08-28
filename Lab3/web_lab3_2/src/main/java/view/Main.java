package view;

import java.util.*;

import controller.ClientController;
import controller.ClientControllerException;
import model.Bill;
import model.Car;
import controller.CarComparator;
public class Main {

	public static <T> void printObjects(List<T> objects) {
		boolean newLineFlag = true;
		if (objects != null && !objects.isEmpty()){
			newLineFlag = !objects.get(0).getClass().getSimpleName().equals("String");
			String str = newLineFlag ? "\n" : "";
			for (T obj : objects)
				System.out.println(obj + str);
		}
	}
	
	public static void carListShow(ClientController controller) {
		List<Car> availableCarList;
		System.out.println("========Available cars========");
		try {
			availableCarList = controller.getAvailableCars();
			printObjects(availableCarList);
			System.out.println("========Sorted by model cars========");
			Collections.sort(availableCarList, new CarComparator());
			printObjects(availableCarList);
		} catch (ClientControllerException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void creatingRequestsShow(ClientController controller) {
		System.out.println("========Create request on cars 5,7,2========");
		try {
			controller.createClientRequest(5, 10);
			controller.createClientRequest(7, 10);
			controller.createClientRequest(2, 10);
			System.out.println("Success");
		} catch (ClientControllerException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("========Create request on car 99========");
		try {
			controller.createClientRequest(99, 10);
		} catch (ClientControllerException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void payBillFunc(ClientController controller, int id) throws ClientControllerException {
		List <String> results;
		results = controller.payClientBill(controller.getClientBills().get(id).getID());
		printObjects(results);
	}
	public static void getAndShowBillsFunc(ClientController controller) throws ClientControllerException {
		System.out.println("========Client bills========");
		List<Bill> bills = controller.getClientBills();
		printObjects(bills);
	}
	public static void payBillsShow(ClientController controller) {
		try {
			getAndShowBillsFunc(controller);
			System.out.println("========Pay two last bills========");
			int size = controller.getClientBills().size();
			payBillFunc(controller, size-1);
			payBillFunc(controller, size-2);
			getAndShowBillsFunc(controller);
		} catch (ClientControllerException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void getAndShowCarsFunc(ClientController controller) throws ClientControllerException {
		System.out.println("========Client cars========");
		List<Car> cars = controller.getClientCars();
		printObjects(cars);
	}
	public static void releaseCarFunc(ClientController controller, int id) throws  ClientControllerException{
		System.out.println("========Release " + id +"-ID car========");
		if(controller.realeseClientCar(id)) {
			System.out.println("The car has been successfully recovered");
		} else {
			System.out.println("Car return failed");
		}
	}
	public static void releaseCarsShow(ClientController controller) {
		try {
			getAndShowCarsFunc(controller);
			releaseCarFunc(controller, 7);
			getAndShowCarsFunc(controller);
			releaseCarFunc(controller, 2);
			releaseCarFunc(controller, 5);
		} catch (ClientControllerException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void setSpecControllerClient(ClientController controller){
		try {
			controller.setClient("МР4091234", false);
		} catch (ClientControllerException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		ClientController controller = new ClientController();
		
		setSpecControllerClient(controller);
		
		carListShow(controller);
		
		creatingRequestsShow(controller);
		
		payBillsShow(controller);
		
		releaseCarsShow(controller);

		controller.close();
	}
}