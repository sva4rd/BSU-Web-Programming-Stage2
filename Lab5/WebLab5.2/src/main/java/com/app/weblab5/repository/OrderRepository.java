package com.app.weblab5.repository;

import com.app.weblab5.model.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query(value = "SELECT o FROM Order o")
    List<Order> getAllOrders();

    @Query(value = "SELECT o FROM Order o WHERE o.id = :orderId")
    List<Order> getOrderById(@Param("orderId") int orderId);

    @Modifying
    @Query(value = "INSERT INTO client_order (id_request) VALUES (:id_request)", nativeQuery = true)
    @Transactional
    void addOrder(@Param("id_request") int id_request);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    int getLastInsertedId();
}
