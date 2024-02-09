package com.vinsguru.test.sec08;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec08.GuessNumberGrpc;
import com.vinsguru.sec08.GuessNumberService;
import com.vinsguru.test.common.AbstractChannelTest;
import io.grpc.Status;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.IntStream;

/*
    It is simply a demo class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GuessANumberTest extends AbstractChannelTest {

    private static final Logger log = LoggerFactory.getLogger(GuessANumberTest.class);
    private final GrpcServer server = GrpcServer.create(new GuessNumberService());
    private GuessNumberGrpc.GuessNumberStub stub;

    @BeforeAll
    public void setup(){
        this.server.start();
        this.stub = GuessNumberGrpc.newStub(channel);
    }

    @RepeatedTest(5)
    public void guessANumberGame(){
        var responseObserver = new GuessResponseHandler();
        var requestObserver = this.stub.makeGuess(responseObserver);
        responseObserver.setRequestObserver(requestObserver);
        responseObserver.start();
        responseObserver.await();
        log.info("--------------");
    }

    @AfterAll
    public void stop(){
        this.server.stop();
    }

}
