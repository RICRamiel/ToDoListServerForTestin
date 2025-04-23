package org.ricramiel.todoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ToDoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToDoServerApplication.class, args);
    }

}
