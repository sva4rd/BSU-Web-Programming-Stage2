package com.app.weblab5.repository;

import com.app.weblab5.model.Car;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository<Car, Long> {
    @Query(value = "SELECT c FROM Car c")
    List<Car> getAllCars();

    @Query(value = "SELECT c FROM Car c WHERE c.available = true")
    List<Car> getAllAvailableCars();

    @Query(value = "SELECT c FROM Car c WHERE c.id = :carId")
    List<Car> getCarById(@Param("carId") int carId);

    @Modifying
    @Query(value = "UPDATE Car c SET c.available = true WHERE c.id = :carId")
    @Transactional
    void releaseCarById(@Param("carId") int carId);

    @Modifying
    @Query(value = "UPDATE Car c SET c.model = :model, c.manufacturer = :manufacturer," +
            "c.state = :state WHERE c.id = :carId")
    @Transactional
    void updateCarById(@Param("model") String model,
                        @Param("manufacturer") String manufacturer,
                        @Param("state") String state,
                        @Param("carId") int carId);

    @Modifying
    @Query(value = "INSERT INTO car (AVAILABLE, MANUFACTURER, MODEL, STATE) " +
            "VALUES (:available, :manufacturer, :model, :state)", nativeQuery = true)
    @Transactional
    void addCar(@Param("available") boolean available, @Param("manufacturer") String manufacturer,
                @Param("model") String model, @Param("state") String state);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    int getLastInsertedId();

    @Modifying
    @Procedure(name = "GetCarsForClient")
    @Transactional
    List<Car> getCarsForClient(@Param("clientPassportData") String clientPassportData);
}
