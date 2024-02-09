package com.vinsguru.sec13.repository;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountRepository {

    private static final Map<Integer, Integer> db = IntStream.rangeClosed(1, 10)
                                                             .boxed()
                                                             .collect(Collectors.toConcurrentMap(
                                                                     Function.identity(),
                                                                     v -> 100
                                                             ));

    public static Integer getBalance(int accountNumber){
        return db.get(accountNumber);
    }

    public static void addAmount(int accountNumber, int amount){
        db.computeIfPresent(accountNumber, (k, v) -> v + amount);
    }

    public static void deductAmount(int accountNumber, int amount){
        db.computeIfPresent(accountNumber, (k, v) -> v - amount);
    }

    public static Map<Integer, Integer> getAllAccounts(){
        return Collections.unmodifiableMap(db);
    }

}
