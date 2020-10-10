package com.vinsguru.game.server;

import com.vinsguru.game.Die;
import com.vinsguru.game.GameServiceGrpc;
import com.vinsguru.game.GameState;
import com.vinsguru.game.Player;
import io.grpc.stub.StreamObserver;

public class GameService extends GameServiceGrpc.GameServiceImplBase {

    @Override
    public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {
        Player client = Player.newBuilder().setName("client").setPosition(0).build();
        Player server = Player.newBuilder().setName("server").setPosition(0).build();
        return new DieStreamingRequest(client, server, responseObserver);
    }

}
