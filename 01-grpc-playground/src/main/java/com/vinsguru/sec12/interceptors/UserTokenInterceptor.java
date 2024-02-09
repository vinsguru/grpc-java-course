package com.vinsguru.sec12.interceptors;

import com.vinsguru.sec12.Constants;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

/*
    user-token-1, user-token-2 => prime users, all calls are allowed
    user-token-3, user-token-4 => standard users, server streaming calls are NOT allowed. other calls are allowed.
    any other token            => not valid...!
 */

public class UserTokenInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UserTokenInterceptor.class);
    private static final Set<String> PRIME_SET = Set.of("user-token-1", "user-token-2");
    private static final Set<String> STANDARD_SET = Set.of("user-token-3", "user-token-4");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        var token = extractToken(metadata.get(Constants.USER_TOKEN_KEY));
        log.info("{}", token); // for demo
        if(!isValid(token)){
            return close(serverCall, metadata, Status.UNAUTHENTICATED.withDescription("token is either null or invalid"));
        }
        var isOneMessage = serverCall.getMethodDescriptor().getType().serverSendsOneMessage();
        if(isOneMessage || PRIME_SET.contains(token)){
            return serverCallHandler.startCall(serverCall, metadata);
        }
        return close(serverCall, metadata, Status.PERMISSION_DENIED.withDescription("user is not allowed to do this operation"));
    }

    private String extractToken(String value){
        return Objects.nonNull(value) && value.startsWith(Constants.BEARER) ?
                value.substring(Constants.BEARER.length()).trim() : null;
    }

    private boolean isValid(String token){
        return Objects.nonNull(token) && (PRIME_SET.contains(token) || STANDARD_SET.contains(token));
    }

    private <ReqT, RespT> ServerCall.Listener<ReqT> close(ServerCall<ReqT, RespT> serverCall, Metadata metadata, Status status){
        serverCall.close(status, metadata);
        return new ServerCall.Listener<ReqT>() {
        };
    }

}
