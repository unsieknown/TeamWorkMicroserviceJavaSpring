package com.mordiniaa.userservice.repositories.mongo.aggregation;

import java.util.Set;
import java.util.UUID;

public interface UserReprCustomRepository {
    boolean allUsersAvailable(UUID... userIds);
    boolean allUsersAvailable(Set<UUID> userIds);
}
