package com.app.weblab5.repository;

import com.app.weblab5.model.Request;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Long> {
    @Query(value = "SELECT r FROM Request r")
    List<Request> getAllRequests();

    @Query(value = "SELECT r FROM Request r WHERE r.id = :requestId")
    List<Request> getRequestById(@Param("requestId") int requestId);

    @Modifying
    @Query(value = "INSERT INTO request (RENT_DAYS, id_car, id_client) " +
            "VALUES (:rent_days, :id_car, :id_client)", nativeQuery = true)
    @Transactional
    void addRequest(@Param("rent_days") int rent_days, @Param("id_car") int id_car,
               @Param("id_client") String id_client);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    int getLastInsertedId();
}
