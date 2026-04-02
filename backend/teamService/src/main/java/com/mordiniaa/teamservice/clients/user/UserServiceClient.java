package com.mordiniaa.teamservice.clients.user;

import com.mordiniaa.teamservice.responses.received.UserDtoCollectionResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Set;
import java.util.UUID;

@HttpExchange
public interface UserServiceClient {

    @GetExchange("/inter/users/exist/{userId}")
    boolean existsUser(@PathVariable UUID userId);

    @PostExchange("/inter/users/batch")
    UserDtoCollectionResponse getUsersByIds(@RequestBody Set<UUID> ids);
}
