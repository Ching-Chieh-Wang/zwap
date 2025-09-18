package com.zwap.user_service.service;

import com.zwap.user_service.dto.RegisterQry;
import com.zwap.user_service.vo.UserVO;


public interface IUserService {
    void register(RegisterQry registerQry) throws Exception;
    UserVO getById(String id);
}
