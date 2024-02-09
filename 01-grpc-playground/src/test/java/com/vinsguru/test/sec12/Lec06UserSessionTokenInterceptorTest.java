package com.vinsguru.test.sec12;

import com.vinsguru.common.GrpcServer;
import com.vinsguru.models.sec12.BalanceCheckRequest;
import com.vinsguru.models.sec12.Money;
import com.vinsguru.models.sec12.WithdrawRequest;
import com.vinsguru.sec12.BankService;
import com.vinsguru.sec12.Constants;
import com.vinsguru.sec12.interceptors.UserTokenInterceptor;
import com.vinsguru.test.common.ResponseObserver;
import io.grpc.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class Lec06UserSessionTokenInterceptorTest extends AbstractInterceptorTest {

    private static final Logger log = LoggerFactory.getLogger(Lec06UserSessionTokenInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return Collections.emptyList();
    }

    @Override
    protected GrpcServer createServer() {
        return GrpcServer.create(6565, builder -> {
            builder.addService(new BankService())
                   .intercept(new UserTokenInterceptor());
        });
    }

    @Test
    public void unaryUserCredentialsDemo(){
        for (int i = 1; i <= 4 ; i++) {
            var request = BalanceCheckRequest.newBuilder()
                                             .setAccountNumber(1)
                                             .build();
            var response = this.bankBlockingStub
                    .withCallCredentials(new UserSessionToken("user-token-" + i))
                    .getAccountBalance(request);
            log.info("{}", response);
        }
    }

    @Test
    public void unaryUserInvalidCredentialsDemo(){
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = BalanceCheckRequest.newBuilder()
                                             .setAccountNumber(1)
                                             .build();
            var response = this.bankBlockingStub
                    .withCallCredentials(new UserSessionToken("user-token-5"))
                    .getAccountBalance(request);
            log.info("{}", response);
        });
        Assertions.assertEquals(Status.Code.UNAUTHENTICATED, ex.getStatus().getCode());
    }


    @Test
    public void streamingUserCredentialsDemo(){
        for (int i = 1; i <= 5 ; i++) {
            var observer = ResponseObserver.<Money>create();
            var request = WithdrawRequest.newBuilder()
                                         .setAccountNumber(i)
                                         .setAmount(30)
                                         .build();
            this.bankStub
                    .withCallCredentials(new UserSessionToken("user-token-" + i))
                    .withdraw(request, observer);
            observer.await();
        }
    }

    private static class UserSessionToken extends CallCredentials {

        private static final String TOKEN_FORMAT = "%s %s";
        private final String jwt;

        public UserSessionToken(String jwt) {
            this.jwt = jwt;
        }

        @Override
        public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
            executor.execute(() -> {
                var metadata = new Metadata();
                metadata.put(Constants.USER_TOKEN_KEY, TOKEN_FORMAT.formatted(Constants.BEARER, jwt));
                metadataApplier.apply(metadata);
            });
        }

    }

}
