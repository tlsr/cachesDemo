package org.example.cache.repo;


import org.example.cache.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    @Override
    List<Customer> findAll();

    Customer findById(long id);
}

