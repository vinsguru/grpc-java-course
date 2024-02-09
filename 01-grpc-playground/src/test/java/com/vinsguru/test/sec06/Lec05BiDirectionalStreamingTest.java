package com.vinsguru.test.sec06;

import com.vinsguru.models.sec06.TransferRequest;
import com.vinsguru.models.sec06.TransferResponse;
import com.vinsguru.models.sec06.TransferStatus;
import com.vinsguru.test.common.ResponseObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Lec05BiDirectionalStreamingTest extends AbstractTest {

    @Test
    public void transferTest() {
        var responseObserver = ResponseObserver.<TransferResponse>create();
        var requestObserver = this.transferStub.transfer(responseObserver);
        var requests = List.of(
                TransferRequest.newBuilder().setAmount(10).setFromAccount(6).setToAccount(6).build(),
                TransferRequest.newBuilder().setAmount(110).setFromAccount(6).setToAccount(7).build(),
                TransferRequest.newBuilder().setAmount(10).setFromAccount(6).setToAccount(7).build(),
                TransferRequest.newBuilder().setAmount(10).setFromAccount(7).setToAccount(6).build()
        );
        requests.forEach(requestObserver::onNext);
        requestObserver.onCompleted();
        responseObserver.await();

        Assertions.assertEquals(4, responseObserver.getItems().size());
        this.validate(responseObserver.getItems().get(0), TransferStatus.REJECTED, 100, 100);
        this.validate(responseObserver.getItems().get(1), TransferStatus.REJECTED, 100, 100);
        this.validate(responseObserver.getItems().get(2), TransferStatus.COMPLETED, 90, 110);
        this.validate(responseObserver.getItems().get(3), TransferStatus.COMPLETED, 100, 100);

    }

    private void validate(TransferResponse response, TransferStatus status, int fromAccountBalance, int toAccountBalance){
        Assertions.assertEquals(status, response.getStatus());
        Assertions.assertEquals(fromAccountBalance, response.getFromAccount().getBalance());
        Assertions.assertEquals(toAccountBalance, response.getToAccount().getBalance());
    }

}
