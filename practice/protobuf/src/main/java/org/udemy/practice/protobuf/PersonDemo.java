package org.udemy.practice.protobuf;

import com.google.protobuf.Int32Value;
import org.udemy.models.Address;
import org.udemy.models.BodyStyle;
import org.udemy.models.Car;
import org.udemy.models.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class PersonDemo {

    public static void main(String[] args) throws IOException {
        final Person person1 = Person.newBuilder().setAge(Int32Value.newBuilder().setValue(5).build()).setName("VinsGuru").setAddress(Address.newBuilder().setCity("Hyd").setPostbox(124).setStreet("Vijay").build()).addCar(Car.newBuilder().setModel("BMW").setMake("German").setBodyStyleValue(BodyStyle.COUPE_VALUE).setYear(1992).build()).addAllCar(List.of(Car.newBuilder().setModel("Japan").setMake("Hyundai").setBodyStyleValue(BodyStyle.SUV_VALUE).setYear(1992).build())).build();

        final Person person2 = Person.newBuilder().setAge(Int32Value.newBuilder().setValue(5)).setName("VinsGuru").build();

        final Person person3 = Person.newBuilder().setAge(Int32Value.newBuilder().setValue(5)).setName("Arfa").build();

        System.out.println("Person 1 equals Person 2 : "+person1.equals(person2));
        System.out.println("Person 2 equals person 3 : "+person2.equals(person3));

        Path path   = Paths.get("sam.ser");
        Files.write(path,person1.toByteArray());

        byte[] bytes = Files.readAllBytes(path);
        Person newSam = Person.parseFrom(bytes);
        System.out.println("New Sam: "+newSam);
    }
}
