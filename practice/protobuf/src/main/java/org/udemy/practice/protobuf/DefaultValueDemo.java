package org.udemy.practice.protobuf;

import org.udemy.models.Person;

public class DefaultValueDemo {

    public static void main(String[] args) {
        Person person =  Person.newBuilder().build();
        System.out.println("City: "+person.getAddress().getCity());
    }
}
