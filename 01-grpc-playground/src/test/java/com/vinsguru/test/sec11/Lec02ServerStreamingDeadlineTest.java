package com.vinsguru.test.sec11;

import com.vinsguru.models.sec11.Money;
import com.vinsguru.models.sec11.WithdrawRequest;
import com.vinsguru.test.common.ResponseObserver;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class Lec02ServerStreamingDeadlineTest extends AbstractTest {

    @Test
    public void blockingDeadlineTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = WithdrawRequest.newBuilder()
                                         .setAccountNumber(1)
                                         .setAmount(50)
                                         .build();
            var iterator = this.bankBlockingStub
                    .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .withdraw(request);
            while (iterator.hasNext()) {
                iterator.next(); // we do not care about the response.
            }
        });
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, ex.getStatus().getCode());
    }

    @Test
    public void asyncDeadlineTest() {
        var observer = ResponseObserver.<Money>create();
        var request = WithdrawRequest.newBuilder()
                                     .setAccountNumber(1)
                                     .setAmount(50)
                                     .build();
        this.bankStub
                .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .withdraw(request, observer);
        observer.await();
        Assertions.assertEquals(2, observer.getItems().size()); // we would have received 2 items
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, Status.fromThrowable(observer.getThrowable()).getCode());
    }

}
