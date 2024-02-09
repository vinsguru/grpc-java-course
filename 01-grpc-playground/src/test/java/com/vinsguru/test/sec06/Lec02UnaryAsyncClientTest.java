package com.vinsguru.test.sec06;

import com.google.protobuf.Empty;
import com.vinsguru.models.sec06.AccountBalance;
import com.vinsguru.models.sec06.AllAccountsResponse;
import com.vinsguru.models.sec06.BalanceCheckRequest;
import com.vinsguru.test.common.ResponseObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec02UnaryAsyncClientTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec02UnaryAsyncClientTest.class);

    @Test
    public void getBalanceTest() {
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        var observer = ResponseObserver.<AccountBalance>create();
        this.bankStub.getAccountBalance(request, observer);
        observer.await();
        Assertions.assertEquals(1, observer.getItems().size());
        Assertions.assertEquals(100, observer.getItems().getFirst().getBalance());
        Assertions.assertNull(observer.getThrowable());
    }

    @Test
    public void allAccountsTest(){
        var observer = ResponseObserver.<AllAccountsResponse>create();
        this.bankStub.getAllAccounts(Empty.getDefaultInstance(), observer);
        observer.await();
        Assertions.assertEquals(1, observer.getItems().size());
        Assertions.assertEquals(10, observer.getItems().getFirst().getAccountsCount());
        Assertions.assertNull(observer.getThrowable());
    }

}
