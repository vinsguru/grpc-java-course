package com.grpcflix.user.service;

import com.grpcflix.user.repository.UserRepository;
import com.vinsguru.grpcflix.common.Genre;
import com.vinsguru.grpcflix.user.UserGenreUpdateRequest;
import com.vinsguru.grpcflix.user.UserResponse;
import com.vinsguru.grpcflix.user.UserSearchRequest;
import com.vinsguru.grpcflix.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository repository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();
        this.repository.findById(request.getLoginId())
                .ifPresent(user -> {
                   builder.setName(user.getName())
                          .setLoginId(user.getLogin())
                          .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();
        this.repository.findById(request.getLoginId())
                .ifPresent(user -> {
                    user.setGenre(request.getGenre().toString());
                    builder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
