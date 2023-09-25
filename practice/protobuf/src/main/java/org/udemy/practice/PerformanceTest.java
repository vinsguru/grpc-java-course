package org.udemy.practice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Int32Value;
import com.google.protobuf.InvalidProtocolBufferException;
import org.udemy.models.Person;
import org.udemy.practice.json.JPerson;

import java.io.IOException;

public class PerformanceTest {
    public static void main(String[] args) throws IOException {
        //JSON
        JPerson person1 = new JPerson();
        person1.setName("Alpha");
        person1.setAge(12);

        ObjectMapper mapper = new ObjectMapper();
        Runnable runnable1 = () -> {
            byte[] bytes = new byte[0];
            try {
                bytes = mapper.writeValueAsBytes(person1);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            try {
                JPerson person2 = mapper.readValue(bytes,JPerson.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };



        //protobuf
        Person person = Person.newBuilder().setAge(Int32Value.newBuilder().setValue(11).build()).setName("Bro").build();
        Runnable runnable2 = () -> {
          byte[] bytes = person.toByteArray();
            try {
                Person person2 = Person.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        };
        runPerformanceTest(runnable1);
        runPerformanceTest(runnable2);
    }


    private static void runPerformanceTest(Runnable runnable){
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 10_00_000 ; i++) {
            runnable.run();
        }

        long time2 = System.currentTimeMillis();
        System.out.printf("Total time: %d ms%n", (time2 - time1));
    }
}
