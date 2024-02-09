package com.vinsguru.sec05.parser;

import com.google.protobuf.InvalidProtocolBufferException;
import com.vinsguru.models.sec05.v1.Television;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class V1Parser {

    private static final Logger log = LoggerFactory.getLogger(V1Parser.class);

    public static void parse(byte[] bytes) throws InvalidProtocolBufferException {
        var tv = Television.parseFrom(bytes);
        log.info("brand: {}", tv.getBrand());
        log.info("year:  {}", tv.getYear());
    }
    
}
