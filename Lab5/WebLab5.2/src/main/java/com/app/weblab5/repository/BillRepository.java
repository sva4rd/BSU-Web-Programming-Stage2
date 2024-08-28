package com.app.weblab5.repository;

import com.app.weblab5.model.Bill;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BillRepository extends CrudRepository<Bill, Long> {
    @Query(value = "SELECT b FROM Bill b")
    List<Bill> getAllBills();

    @Query(value = "SELECT b FROM Bill b WHERE b.id = :id_bill")
    List<Bill> getBillById(@Param("id_bill") int id_bill);

    @Query(value = "SELECT b FROM Bill b WHERE b.client.passport_data = :id_client AND b.is_paid = false")
    List<Bill> getBillsByClientId(@Param("id_client") int id_client);

    @Modifying
    @Query(value = "INSERT INTO bill (is_paid, id_client, id_order) " +
            "VALUES (:is_paid, :id_client, :id_order)", nativeQuery = true)
    @Transactional
    void addBill(@Param("is_paid") boolean is_paid, @Param("id_client") String id_client,
               @Param("id_order") int id_order);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    int getLastInsertedId();
}
