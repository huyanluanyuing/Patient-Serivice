package com.pm.billingservice.grpc;

import billing.BillingRespond;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
                                     StreamObserver<BillingRespond> responseObserver) {
        log.info("Received billing account creation request for userId: {}", billingRequest.toString());
        BillingRespond respond = BillingRespond.newBuilder()
                .build();
        responseObserver.onNext(respond);
        responseObserver.onCompleted();
    }
}
