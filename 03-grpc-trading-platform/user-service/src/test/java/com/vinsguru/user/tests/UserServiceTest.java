package com.vinsguru.user.tests;

import com.vinsguru.common.Ticker;
import com.vinsguru.user.StockTradeRequest;
import com.vinsguru.user.TradeAction;
import com.vinsguru.user.UserInformationRequest;
import com.vinsguru.user.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "grpc.server.port=-1",
        "grpc.server.in-process-name=integration-test",
        "grpc.client.user-service.address=in-process:integration-test"
})
public class UserServiceTest {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

    @Test
    public void userInformationTest() {
        var request = UserInformationRequest.newBuilder()
                                            .setUserId(1)
                                            .build();
        var response = this.stub.getUserInformation(request);
        Assertions.assertEquals(10_000, response.getBalance());
        Assertions.assertEquals("Sam", response.getName());
        Assertions.assertTrue(response.getHoldingsList().isEmpty());
    }

    @Test
    public void unknownUserTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = UserInformationRequest.newBuilder()
                                                .setUserId(10)
                                                .build();
            var response = this.stub.getUserInformation(request);
        });
        Assertions.assertEquals(Status.Code.NOT_FOUND, ex.getStatus().getCode());
    }

    @Test
    public void unknownTickerBuyTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = StockTradeRequest.newBuilder()
                                           .setUserId(1)
                                           .setPrice(1)
                                           .setQuantity(1)
                                           .setAction(TradeAction.BUY)
                                           .build();
            this.stub.tradeStock(request);
        });
        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());
    }

    @Test
    public void insufficientSharesTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = StockTradeRequest.newBuilder()
                                           .setUserId(1)
                                           .setPrice(1)
                                           .setQuantity(1000)
                                           .setTicker(Ticker.AMAZON)
                                           .setAction(TradeAction.SELL)
                                           .build();
            this.stub.tradeStock(request);
        });
        Assertions.assertEquals(Status.Code.FAILED_PRECONDITION, ex.getStatus().getCode());
    }

    @Test
    public void insufficientBalanceTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = StockTradeRequest.newBuilder()
                                           .setUserId(1)
                                           .setPrice(1)
                                           .setQuantity(10001)
                                           .setTicker(Ticker.AMAZON)
                                           .setAction(TradeAction.BUY)
                                           .build();
            this.stub.tradeStock(request);
        });
        Assertions.assertEquals(Status.Code.FAILED_PRECONDITION, ex.getStatus().getCode());
    }

    @Test
    public void buySellTest(){
        // buy
        var buyRequest = StockTradeRequest.newBuilder()
                                       .setUserId(2)
                                       .setPrice(100)
                                       .setQuantity(5)
                                       .setTicker(Ticker.AMAZON)
                                       .setAction(TradeAction.BUY)
                                       .build();
        var buyResponse = this.stub.tradeStock(buyRequest);

        // validate balance
        Assertions.assertEquals(9500, buyResponse.getBalance());

        // check holding
        var userRequest = UserInformationRequest.newBuilder().setUserId(2).build();
        var userResponse = this.stub.getUserInformation(userRequest);
        Assertions.assertEquals(1, userResponse.getHoldingsCount());
        Assertions.assertEquals(Ticker.AMAZON, userResponse.getHoldingsList().getFirst().getTicker());

        // sell
        var sellRequest = buyRequest.toBuilder().setAction(TradeAction.SELL).setPrice(102).build();
        var sellResponse = this.stub.tradeStock(sellRequest);

        // validate balance
        Assertions.assertEquals(10010, sellResponse.getBalance());
    }

}
