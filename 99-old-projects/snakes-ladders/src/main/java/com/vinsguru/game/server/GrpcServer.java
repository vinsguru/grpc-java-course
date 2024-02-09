package com.vinsguru.game.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.Executors;

public class GrpcServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(6565)
                .executor(Executors.newFixedThreadPool(20))
                .addService(new GameService())
                .build();

        server.start();

        server.awaitTermination();

    }

}
