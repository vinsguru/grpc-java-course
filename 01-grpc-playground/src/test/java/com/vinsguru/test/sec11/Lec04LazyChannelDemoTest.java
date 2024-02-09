package com.vinsguru.test.sec11;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec11.BalanceCheckRequest;
import com.vinsguru.models.sec11.BankServiceGrpc;
import com.vinsguru.sec11.DeadlineBankService;
import com.vinsguru.test.common.AbstractChannelTest;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    It is a class to demo the lazy channel creation behavior
 */
public class Lec04LazyChannelDemoTest extends AbstractChannelTest {

    private static final Logger log = LoggerFactory.getLogger(Lec04LazyChannelDemoTest.class);
    private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
    private BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;

    @BeforeAll
    public void setup() {
      //  this.grpcServer.start();
        this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void lazyChannelDemo() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = BalanceCheckRequest.newBuilder()
                                             .setAccountNumber(1)
                                             .build();
            var response = this.bankBlockingStub.getAccountBalance(request);
            log.info("{}", response);
        });
        Assertions.assertEquals(Status.Code.UNAVAILABLE, ex.getStatus().getCode());
    }

    @AfterAll
    public void stop() {
        this.grpcServer.stop();
    }

}
