package com.vinsguru.test.sec12.interceptors;

import io.grpc.*;

public class GzipRequestInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions,
                                                               Channel channel) {
        return channel.newCall(methodDescriptor, callOptions.withCompression("gzip"));
    }

}
