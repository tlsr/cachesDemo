package org.example.cache.repo;

import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Generator {

    @Test
    @DisplayName("")
    void test() {

        Faker faker = new Faker();
        for (int i = 0; i < 1000; i++) {
            Name name = faker.name();

            String firstName = name.firstName().replaceAll("'", "''");
            String lastName = name.lastName().replaceAll("'", "''");
            System.out.println("INSERT INTO customer (id, first_name, last_name, email) VALUES (" + i + ", '" + firstName + "', '" + lastName + "', '" + firstName + lastName + i + "@test.com');");
        }
    }

}
