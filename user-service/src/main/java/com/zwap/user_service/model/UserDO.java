package com.zwap.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDO {
    private String uid;
    private String name;
    private String email;
    private String photoUrl;
}