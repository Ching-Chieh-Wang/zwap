package com.zwap.user_service.controller;

import com.zwap.user_service.dto.RegisterQry;
import com.zwap.user_service.service.IUserService;
import com.zwap.user_service.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping()
    public void register(@Valid @RequestBody RegisterQry registerQry) throws Exception {
        userService.register(registerQry);
    }

    @GetMapping("/{id}")
    public UserVO getById(@PathVariable("id") String id) {
        return userService.getById(id);
    }

}
