package com.vinsguru.test.sec12;

import com.vinsguru.models.sec12.Money;
import com.vinsguru.models.sec12.WithdrawRequest;
import com.vinsguru.models.sec12.BalanceCheckRequest;
import com.vinsguru.test.common.ResponseObserver;
import com.vinsguru.test.sec12.interceptors.DeadlineInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.Deadline;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
    It is a class to demo interceptor
 */
public class Lec03DeadlineInterceptorTest extends AbstractInterceptorTest {

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(new DeadlineInterceptor(Duration.ofSeconds(2)));
    }

    @Test
    public void defaultDeadlineDemo(){
        var request = BalanceCheckRequest.newBuilder()
                                         .setAccountNumber(1)
                                         .build();
        var response = this.bankBlockingStub.getAccountBalance(request);
    }

    @Test
    public void overrideInterceptorDemo(){
        var observer = ResponseObserver.<Money>create();
        var request = WithdrawRequest.newBuilder()
                                     .setAccountNumber(1)
                                     .setAmount(50)
                                     .build();
        this.bankStub
                .withDeadline(Deadline.after(6, TimeUnit.SECONDS))
                .withdraw(request, observer);
        observer.await();
    }

}
