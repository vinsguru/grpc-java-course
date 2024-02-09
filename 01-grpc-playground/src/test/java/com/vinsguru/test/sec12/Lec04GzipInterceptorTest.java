package com.vinsguru.test.sec12;

import com.vinsguru.models.sec12.BalanceCheckRequest;
import com.vinsguru.test.sec12.interceptors.GzipRequestInterceptor;
import io.grpc.ClientInterceptor;
import org.junit.jupiter.api.Test;

import java.util.List;

/*
    It is a class to demo enabling gzip compression via interceptor
    Update logback xml with DEBUG mode
 */
public class Lec04GzipInterceptorTest extends AbstractInterceptorTest {

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(new GzipRequestInterceptor());
    }

    @Test
    public void gzipDemo() {
        var request = BalanceCheckRequest.newBuilder()
                                         .setAccountNumber(1)
                                         .build();
        var response = this.bankBlockingStub.getAccountBalance(request);
    }

}
