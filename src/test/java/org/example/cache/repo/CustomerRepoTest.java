package org.example.cache.repo;

import org.example.cache.entities.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CustomerRepoTest {

    @Autowired
    CustomerRepository repo;

    @Test
    @DisplayName("Find by first name should return 2 customers")
    void test() {
        //given
        //when
        List<Customer> res = repo.findByLastName("Smith");
        //then
        assertEquals(2, res.size());
    }

    @Test
    @DisplayName("Find all should return 1000 customers")
    void test1() {
        //given
        //when
        List<Customer> all = repo.findAll();
        //then
        assertEquals(1000, all.size());
    }

}
