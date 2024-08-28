package com.app.weblab5.repository;

import com.app.weblab5.model.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    @Query(value = "SELECT c FROM Client c")
    List<Client> getAllClients();

    @Query(value = "SELECT c FROM Client c WHERE c.passport_data = :clientId")
    List<Client> getClientById(@Param("clientId") String clientId);

    @Query(value = "SELECT c FROM Client c WHERE c.id_user = :id")
    List<Client> getClientByUserID(@Param("id") int id);

    @Modifying
    @Query(value = "INSERT INTO rent_client (PASSPORT_DATA) VALUES (:passport_data)", nativeQuery = true)
    @Transactional
    void addClient(@Param("passport_data") String passport_data);

    @Modifying
    @Procedure(name = "PayBill")
    @Transactional
    List<String> payBill(@Param("bill_id") int bill_id);
}
