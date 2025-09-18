package com.zwap.user_service.grpc;


import com.zwap.user_service.proto.User.GetUserByIdRequest;
import com.zwap.user_service.proto.User.GetUserByIdResponse;
import com.zwap.user_service.proto.UserServiceGrpc;
import com.zwap.user_service.service.IUserService;
import com.zwap.user_service.vo.UserVO;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {


    @Autowired
    private IUserService userService;

    @Override
    public void getUserById(GetUserByIdRequest request,
                            StreamObserver<GetUserByIdResponse> responseObserver) {
        try {
            UserVO uservo = userService.getById(request.getId());

            GetUserByIdResponse response = GetUserByIdResponse.newBuilder()
                    .setDisplayName(uservo.getName())
                    .setPhotoUrl(uservo.getPhotoUrl() != null ? uservo.getPhotoUrl() : "")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
