package model;

import java.util.List;

import jakarta.persistence.*;

import connection.ConnectionPoolException;
import controller.Admin;
import modelDAO.BillDAO;
import modelDAO.CarDAO;
import modelDAO.ClientDAO;
import modelDAO.DAOException;

@Entity
@Table(name = "rent_client")
@NamedStoredProcedureQuery(
	    name = "PayBill",
	    procedureName = "PayBill",
	    parameters = {
	        @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "bill_id")
	    },
		resultSetMappings = "PayBillResult"
)
@SqlResultSetMapping(
		name = "PayBillResult",
		columns = @ColumnResult(name = "result", type = String.class)
)
@NamedQueries({
	@NamedQuery(
			name = "getAllClients",
			query = "SELECT c FROM Client c"
			),
	@NamedQuery(
			name = "getClientById",
			query = "SELECT c FROM Client c WHERE c.passport_data = :clientId"
			)
})
public class Client {
	@Id
	String passport_data;

	public Client(String passport_data, boolean isNew) throws ConnectionPoolException, DAOException {
		this.passport_data = passport_data;
		if (isNew) {
			ClientDAO.insertClient(this);
		}
	}

	public Client() {

	}

	public String getPassportData() {
		return this.passport_data;
	}

	public List<Car> getRentCars() throws ConnectionPoolException, DAOException{
		return CarDAO.getCarsForClient(passport_data);
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
		return BillDAO.getBillsByClientId(passport_data);
	}

	public Request createRequest(Car car, int rentalPeriod) throws ConnectionPoolException, DAOException {
		return new Request(this, car, rentalPeriod);
	}

	public List<String> payBill(int billID) throws ConnectionPoolException, DAOException {
		List<String> results = ClientDAO.payBill(billID);
		Admin.updateAvailableCars();
		return results;
	}
	
	@Override
	public String toString() {
	    return "Passport data: " + getPassportData();
	}
}
