package com.vinsguru.client.rpctypes;

import com.vinsguru.models.Money;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingResponse implements StreamObserver<Money> {

    private CountDownLatch latch;

    public MoneyStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Money money) {
        System.out.println(
                "Received async : " + money.getValue()
        );
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(
                throwable.getMessage()
        );
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println(
                "Server is done!!"
        );
        latch.countDown();
    }
}
