package model;

import java.util.List;

import connection.ConnectionPoolException;
import controller.Admin;
import modelDAO.BillDAO;
import modelDAO.CarDAO;
import modelDAO.ClientDAO;
import modelDAO.DAOException;

public class Client {
	String passportData;

	public Client(String passportData, boolean isNew) throws ConnectionPoolException, DAOException {
		this.passportData = passportData;
		if (isNew) {
			ClientDAO.insertClient(this);
		}
	}

	public String getPassportData() {
		return this.passportData;
	}

	public List<Car> getRentCars() throws ConnectionPoolException, DAOException{
		return CarDAO.getCarsForClient(passportData);
	}

	public boolean releaseCar(int carID) throws ConnectionPoolException, DAOException {
		List<Car> cars = getRentCars();
		boolean success = false;
		for (Car c : cars) {
			if (c.getID() == carID) {
				CarDAO.releaseCarById(carID);
				success = true;
				break;
			}
		}
		return success;
	}

	public List<Bill> getBills() throws ConnectionPoolException, DAOException{
		return BillDAO.getBillsByClientId(passportData);
	}

	public Request createRequest(Car car, int rentalPeriod) throws ConnectionPoolException, DAOException {
		return new Request(this, car, rentalPeriod);
	}

	public void payBill(int billID) throws ConnectionPoolException, DAOException {
		ClientDAO.payBill(billID);
		Admin.updateAvailableCars();
	}
	
	@Override
	public String toString() {
	    return "Passport data: " + getPassportData();
	}
}
