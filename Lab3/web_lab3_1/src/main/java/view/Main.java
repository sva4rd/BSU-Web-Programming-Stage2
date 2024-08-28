package view;

import java.util.*;

import controller.ClientController;
import controller.ClientControllerException;
import model.Bill;
import model.Car;
import controller.CarComparator;

public class Main {
	
	public static void carListShow(ClientController controller) {
		List<Car> availableCarList = new ArrayList<>();
		System.out.println("========Available cars========");
		try {
			availableCarList = controller.getAvailableCars();
			System.out.println("========Available cars========");
			for (Car car : availableCarList) {
				System.out.println(car + "\n");
			}
			
			System.out.println("========Sorted by model cars========");
			Collections.sort(availableCarList, new controller.CarComparator());
			for (Car car : availableCarList) {
				System.out.println(car + "\n");
			}
		} catch (ClientControllerException e) {
			System.err.println(e);
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
			System.err.println(e);
		}
		System.out.println("========Create request on car 99========");
		try {
			controller.createClientRequest(99, 10);
		} catch (ClientControllerException e) {
			System.err.println(e);
		}
	}
	
	public static void showPayingBillResult(List <String> results) {
		for (String s : results)
			System.out.println(s);
	}
	
	public static void payBillsShow(ClientController controller) {
		System.out.println("========Client bills========");
		try {
			List<Bill> bills = controller.getClientBills();
			for (Bill bill : bills) {
				System.out.println(bill + "\n");
			}
			System.out.println("========Pay two last bills========");
			List <String> results = new ArrayList<>();
			int size = controller.getClientBills().size();
			results = controller.payClientBill(controller.getClientBills().get(size-1).getID());
			showPayingBillResult(results);
			results = controller.payClientBill(controller.getClientBills().get(size-2).getID());
			showPayingBillResult(results);
			
			bills = controller.getClientBills();
			System.out.println("========Client bills========");
			for (Bill bill : bills) {
				System.out.println(bill + "\n");
			}
		} catch (ClientControllerException e) {
			System.err.println(e);
		}
	}
	
	public static void releaseCarsShow(ClientController controller) {
		System.out.println("========Client cars========");
		try {
			List<Car> cars = controller.getClientCars();
			for (Car car : cars) {
				System.out.println(car + "\n");
			}
			
			System.out.println("========Release 7-ID car========");
			if(controller.realeseClientCar(7)) {
				System.out.println("The car has been successfully recovered");
			} else {
				System.out.println("Car return failed");
			}
			
			cars = controller.getClientCars();
			System.out.println("========Client cars========");
			for (Car car : cars) {
				System.out.println(car + "\n");
			}
			
			System.out.println("========Release 2-ID and 5-ID car========");
			if(controller.realeseClientCar(2)) {
				System.out.println("The car has been successfully recovered");
			} else {
				System.out.println("Car return failed");
			}
			if(controller.realeseClientCar(5)) {
				System.out.println("The car has been successfully recovered");
			} else {
				System.out.println("Car return failed");
			}
		} catch (ClientControllerException e) {
			System.err.println(e);
		}
	}
	
	public static void setControllerClient(ClientController controller){
		try {
			controller.setClient("МР4091234", false);
		} catch (ClientControllerException e) {
			System.err.println(e);
		}
	}
	
	public static void main(String[] args) {
		ClientController controller = new ClientController();
		
		setControllerClient(controller);
		
		//carListShow(controller);
		
		creatingRequestsShow(controller);
		
		payBillsShow(controller);
		
		releaseCarsShow(controller);

		controller.close();
	}
}