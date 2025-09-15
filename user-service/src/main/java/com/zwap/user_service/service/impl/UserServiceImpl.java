package com.zwap.user_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zwap.user_service.convertor.UserConverter;
import com.zwap.user_service.dto.RegisterQry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.zwap.user_service.exception.UserNotFoundException;
import com.zwap.user_service.exception.UserServiceException;
import com.zwap.user_service.model.UserDO;
import com.zwap.user_service.repository.UserRepository;
import com.zwap.user_service.service.IUserService;
import com.zwap.user_service.constant.RedisPrefix;
import com.zwap.user_service.constant.RedisTTL;
import com.zwap.user_service.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objMapper;

    private static final String NOT_FOUND = "__NOT_FOUND__";

    public void register(@Valid RegisterQry registerQry) throws Exception {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(registerQry.getIdToken());

        String name = decodedToken.getName();
        String photoUrl = (String) decodedToken.getClaims().get("picture");

        if (name == null || name.isEmpty()) {
            name = registerQry.getName();
        }
        if (photoUrl == null || photoUrl.isEmpty()) {
            photoUrl = registerQry.getPhotoUrl();
        }

        Firestore db = FirestoreClient.getFirestore();

        UserDO userData = new UserDO(
                decodedToken.getUid(),
                name,
                decodedToken.getEmail(),
                photoUrl
        );

        db.collection("users").document(decodedToken.getUid()).set(userData).get();
    }

    @Override
    public UserVO getById(String id) {
        final String userKey = RedisPrefix.USER.getPrefix() + id;

        // Step 1: Check cache first (cache-aside pattern)
        String cachedUser = stringRedisTemplate.opsForValue().get(userKey);
        if (cachedUser == null) {
            // Step 2: Cache miss, fetch from Firestore (DB)
            try {
                UserDO userDO = userRepository.findById(id);

                if (userDO == null) {
                    // Step 3: User not found in Firestore, cache a NOT_FOUND marker to prevent cache penetration (dogpile)
                    stringRedisTemplate.opsForValue().set(userKey, NOT_FOUND, RedisTTL.NOT_FOUND.getJitteredDuration());
                    // The NOT_FOUND string is used as a placeholder in the cache to indicate absence and avoid repeated DB hits.
                    throw new UserNotFoundException("User not found with id: " + id);
                }

                // Step 4: User found, convert and cache the result
                UserVO userVO = UserConverter.toVO(userDO);
                String userJson = objMapper.writeValueAsString(userVO);
                stringRedisTemplate.opsForValue().set(userKey, userJson, RedisTTL.USER.getJitteredDuration());
                return userVO;
            } catch (JsonProcessingException e) {
                // Wrap serialization issues in a service exception to indicate a server-side error.
                throw new UserServiceException("Failed to serialize UserVO for id: " + id, e);
            }
        } else {
            // Step 5: Cache hit, check if we cached NOT_FOUND marker
            if (NOT_FOUND.equals(cachedUser)) {
                // The NOT_FOUND string signals the user does not exist, so we throw not found exception.
                throw new UserNotFoundException("User not found with id: " + id);
            }
            // Step 6: Deserialize and return cached user data
            return parseJson(cachedUser);
        }
    }

    private UserVO parseJson(String json) {
        try {
            return objMapper.readValue(json, UserVO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse UserVO from cache JSON", e);
        }
    }


}
