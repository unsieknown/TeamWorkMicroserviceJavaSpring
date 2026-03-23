package com.mordiniaa.userservice.services;

import com.mordiniaa.userservice.exceptions.UsersNotAvailableException;
import com.mordiniaa.userservice.models.mongodb.UserRepresentation;
import com.mordiniaa.userservice.models.mysql.User;
import com.mordiniaa.userservice.repositories.mongo.UserRepresentationRepository;
import com.mordiniaa.userservice.repositories.mongo.aggregation.UserReprCustomRepository;
import com.mordiniaa.userservice.repositories.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MongoUserService {

    private final UserReprCustomRepository userReprCustomRepository;
    private final UserRepository userRepository;
    private final UserRepresentationRepository userRepresentationRepository;
    private final MongoTemplate mongoTemplate;

    public void checkUserAvailability(UUID... userIds) {
        boolean result = userReprCustomRepository.allUsersAvailable(userIds);
        if (!result) {
            throw new UsersNotAvailableException();
        }
    }

    public void createUserRepresentation(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User Not Found"));

        UserRepresentation mongoUser = new UserRepresentation();
        mongoUser.setUsername(user.getUsername());
        mongoUser.setImageKey(user.getImageKey());
        mongoUser.setUserId(user.getUserId());
        userRepresentationRepository.save(mongoUser);
    }

    public void setProfileImageKey(UUID userId, String imageKey) {

        Query query = Query.query(
                Criteria.where("userId").is(userId)
        );
        Update update = new Update()
                .set("imageKey", imageKey);

        mongoTemplate.updateFirst(query, update, UserRepresentation.class);
    }

    public void updateUsername(UUID userId, String username) {

        Query query = Query.query(
                Criteria.where("userId").is(userId)
        );
        Update update = new Update()
                .set("username", username);
        mongoTemplate.updateFirst(query, update, UserRepresentation.class);
    }

    public void deleteUser(UUID userId) {

        Query query = Query.query(
                Criteria.where("userId").is(userId)
        );
        Update update = new Update()
                .set("deleted", true);
        mongoTemplate.updateFirst(query, update, UserRepresentation.class);
    }
}
