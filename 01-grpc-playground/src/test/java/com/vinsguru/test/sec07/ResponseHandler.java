package com.vinsguru.test.sec07;

import com.google.common.util.concurrent.Uninterruptibles;
import com.vinsguru.models.sec07.Output;
import com.vinsguru.models.sec07.RequestSize;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ResponseHandler implements StreamObserver<Output> {

    private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);
    private final CountDownLatch latch = new CountDownLatch(1);
    private StreamObserver<RequestSize> requestObserver;
    private int size;

    @Override
    public void onNext(Output output) {
        this.size--;
        this.process(output);
        if(this.size == 0){
            log.info("---------------");
            this.request(ThreadLocalRandom.current().nextInt(1, 6));
        }
    }

    @Override
    public void onError(Throwable throwable) {
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        this.requestObserver.onCompleted();
        log.info("completed");
        latch.countDown();
    }

    public void setRequestObserver(StreamObserver<RequestSize> requestObserver) {
        this.requestObserver = requestObserver;
    }

    private void request(int size){
        log.info("requesting size {}", size);
        this.size = size;
        this.requestObserver.onNext(RequestSize.newBuilder().setSize(size).build());
    }

    private void process(Output output){
        log.info("received {}", output);
        Uninterruptibles.sleepUninterruptibly(
                ThreadLocalRandom.current().nextInt(50, 200),
                TimeUnit.MILLISECONDS
        );
    }

    public void await(){
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        this.request(3);
    }

}
