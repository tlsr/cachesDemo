package org.example.cache.repo;


import org.example.cache.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    Customer findByEmail(String email);

    @Override
    List<Customer> findAll();

}

