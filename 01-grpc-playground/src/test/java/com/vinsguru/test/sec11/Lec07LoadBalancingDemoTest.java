package com.vinsguru.test.sec11;

import com.vinsguru.models.sec06.BalanceCheckRequest;
import com.vinsguru.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    It is a class to demo nginx load balancing
    Ensure that you run the 2 bank service instance
    1. use sec06 bank service
    2. run 2 instances. 1 on port 6565 and other on 7575
    3. start nginx (src/test/resources). nginx listens on port 8585
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec07LoadBalancingDemoTest {

    private static final Logger log = LoggerFactory.getLogger(Lec07LoadBalancingDemoTest.class);
    private BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
    private ManagedChannel channel;

    @BeforeAll
    public void setup() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 8585)
                                            .usePlaintext()
                                            .build();
        this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    // I do not want to run this as part of mvn test
    //@Test
    public void loadBalancingDemo() {
        for (int i = 1; i <= 10 ; i++) {
            var request = BalanceCheckRequest.newBuilder()
                                             .setAccountNumber(i)
                                             .build();
            var response = this.bankBlockingStub.getAccountBalance(request);
            log.info("{}", response);
        }
    }

    @AfterAll
    public void stop() {
        this.channel.shutdownNow();
    }

}
