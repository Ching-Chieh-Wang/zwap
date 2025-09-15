package com.zwap.user_service.convertor;

import com.zwap.user_service.model.UserDO;
import com.zwap.user_service.vo.UserVO;
import org.springframework.beans.BeanUtils;

public class UserConverter {
    public static UserVO toVO(UserDO userDO) {
        if (userDO == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDO, userVO);
        return userVO;
    }
}
