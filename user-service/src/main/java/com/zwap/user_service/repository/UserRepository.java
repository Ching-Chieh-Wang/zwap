package com.zwap.user_service.repository;

import com.google.cloud.firestore.Firestore;
import com.zwap.user_service.exception.UserRepositoryException;
import com.zwap.user_service.model.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final Firestore db;

    @Autowired
    public UserRepository(Firestore db) {
        this.db = db;
    }

    public UserDO findById(String id) {
        try {
            return db.collection("users").document(id)
                    .get()
                    .get()
                    .toObject(UserDO.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new UserRepositoryException("Thread was interrupted while fetching user by ID: " + id, e);
        } catch (java.util.concurrent.ExecutionException e) {
            throw new UserRepositoryException("Error occurred while fetching user by ID: " + id, e);
        }
    }
}

