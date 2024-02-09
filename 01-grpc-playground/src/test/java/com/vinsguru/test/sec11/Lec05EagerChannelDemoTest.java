package com.vinsguru.test.sec11;

import com.vinsguru.test.common.AbstractChannelTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    It is a class to demo the eager channel creation behavior.
    There is a bug: https://github.com/grpc/grpc-java/issues/10517
 */
public class Lec05EagerChannelDemoTest extends AbstractChannelTest {

    private static final Logger log = LoggerFactory.getLogger(Lec05EagerChannelDemoTest.class);

    @Test
    public void eagerChannelDemo() {
        log.info("{}", channel.getState(true));
    }

}
