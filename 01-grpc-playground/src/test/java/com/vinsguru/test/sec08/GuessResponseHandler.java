package com.vinsguru.test.sec08;

import com.vinsguru.models.sec08.GuessRequest;
import com.vinsguru.models.sec08.GuessResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class GuessResponseHandler implements StreamObserver<GuessResponse> {

    private static final Logger log = LoggerFactory.getLogger(GuessResponseHandler.class);
    private final CountDownLatch latch = new CountDownLatch(1);
    private StreamObserver<GuessRequest> requestObserver;
    private int lower;
    private int upper;
    private int middle;

    @Override
    public void onNext(GuessResponse guessResponse) {
        log.info("attempt: {}, result: {}", guessResponse.getAttempt(), guessResponse.getResult());
        switch (guessResponse.getResult()){
            case TOO_LOW -> this.send(this.middle, this.upper);
            case TOO_HIGH -> this.send(this.lower, this.middle);
            // we can ignore correct. as the game is over.
        }
    }

    @Override
    public void onError(Throwable throwable) {
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        requestObserver.onCompleted();
        latch.countDown();
    }

    private void send(int low, int high){
        this.lower = low;
        this.upper = high;
        this.middle = low + (high - low) / 2;
        log.info("client guessed {}", this.middle);
        this.requestObserver.onNext(GuessRequest.newBuilder().setGuess(this.middle).build());
    }

    public void start(){
        this.send(0, 100);
    }

    public void await(){
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRequestObserver(StreamObserver<GuessRequest> requestObserver) {
        this.requestObserver = requestObserver;
    }
}
