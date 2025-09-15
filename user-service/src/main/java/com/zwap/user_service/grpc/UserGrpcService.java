package com.zwap.user_service.grpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zwap.user_service.proto.GetUserByIdRequest;
import com.zwap.user_service.proto.GetUserByIdResponse;
import com.zwap.user_service.proto.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;


@Service
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void getUserById(GetUserByIdRequest request,
                            StreamObserver<GetUserByIdResponse> responseObserver) {
        String cached = redisTemplate.opsForValue().get(request.getId());
        GetUserByIdResponse response;

        if (cached != null) {
            try {
                response = objectMapper.readValue(cached, GetUserByIdResponse.class);
            } catch (JsonProcessingException e) {
                // fallback if cached JSON is broken
                response = GetUserByIdResponse.newBuilder().build();
            }
        } else {
            try {
                // Fetch user from Firebase
                UserRecord userRecord = FirebaseAuth.getInstance().getUser(request.getId());

                response = GetUserByIdResponse.newBuilder()
                        .setDisplayName(userRecord.getDisplayName())
                        .setPhotoUrl(userRecord.getPhotoUrl())
                        .build();

                // Cache in Redis
                redisTemplate.opsForValue().set(userRecord.getUid(), objectMapper.writeValueAsString(response));

            } catch (FirebaseAuthException | JsonProcessingException e) {
                response = GetUserByIdResponse.newBuilder().build();
            }
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
