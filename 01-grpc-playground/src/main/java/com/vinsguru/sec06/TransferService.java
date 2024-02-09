package com.vinsguru.sec06;

import com.vinsguru.models.sec06.TransferRequest;
import com.vinsguru.models.sec06.TransferResponse;
import com.vinsguru.models.sec06.TransferServiceGrpc;
import com.vinsguru.sec06.requesthandlers.TransferRequestHandler;
import io.grpc.stub.StreamObserver;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferRequestHandler(responseObserver);
    }

}
