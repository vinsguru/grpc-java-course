package com.vinsguru.sec02;

import com.vinsguru.models.sec02.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoDemo {

    private static final Logger log = LoggerFactory.getLogger(ProtoDemo.class);

    public static void main(String[] args) {

        // create person1
        var person1 = createPerson();

        // create another instance with same values
        var person2 = createPerson();

        // compare
        log.info("equals {}", person1.equals(person2));
        log.info("== {}", (person1 == person2));

        // mutable? No

        // create another instance with diff values
        var person3 = person1.toBuilder().setName("mike").build();
        log.info("person3: {}", person3);

        // compare
        log.info("equals {}", person1.equals(person3));
        log.info("== {}", (person1 == person3));

        //null?
        var person4 = person1.toBuilder().clearName().build();
        log.info("person4: {}", person4);

    }

    private static Person createPerson() {
        return Person.newBuilder()
                     .setName("sam")
                     .setAge(12)
                     .build();
    }

}
