package com.vinsguru.sec03;

import com.vinsguru.models.sec03.Address;
import com.vinsguru.models.sec03.School;
import com.vinsguru.models.sec03.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec04Composition {

    private static final Logger log = LoggerFactory.getLogger(Lec04Composition.class);

    public static void main(String[] args) {

        // create student
        var address = Address.newBuilder()
                .setStreet("123 main st")
                .setCity("atlanta")
                .setState("GA")
                .build();
        var student = Student.newBuilder()
                .setName("sam")
                .setAddress(address)
                .build();
        // create school
        var school = School.newBuilder()
                .setId(1)
                .setName("high school")
                .setAddress(address.toBuilder().setStreet("234 main st").build())
                .build();

        log.info("school: {}", school);
        log.info("student: {}", student);

    }

}
