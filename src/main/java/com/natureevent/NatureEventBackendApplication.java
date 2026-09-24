package com.natureevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NatureEventBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NatureEventBackendApplication.class, args);
    }
    
    //note
    //first -entity(user)-repository (user)->(interface)-service-serviceImpl
}
