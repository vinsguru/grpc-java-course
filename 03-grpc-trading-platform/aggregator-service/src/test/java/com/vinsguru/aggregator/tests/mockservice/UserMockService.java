package com.vinsguru.aggregator.tests.mockservice;

import com.vinsguru.user.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class UserMockService extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void getUserInformation(UserInformationRequest request, StreamObserver<UserInformation> responseObserver) {
        if (request.getUserId() == 1) {
            var user = UserInformation.newBuilder()
                                      .setUserId(1)
                                      .setBalance(100)
                                      .setName("integration-test")
                                      .build();
            responseObserver.onNext(user);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    @Override
    public void tradeStock(StockTradeRequest request, StreamObserver<StockTradeResponse> responseObserver) {
        var response = StockTradeResponse.newBuilder()
                                         .setUserId(request.getUserId())
                                         .setTicker(request.getTicker())
                                         .setAction(request.getAction())
                                         .setPrice(request.getPrice())
                                         .setQuantity(request.getQuantity())
                                         .setTotalPrice(1000)
                                         .setBalance(0)
                                         .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
