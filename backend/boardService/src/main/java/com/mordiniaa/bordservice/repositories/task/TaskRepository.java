package com.mordiniaa.bordservice.repositories.task;

import com.mordiniaa.bordservice.models.task.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, ObjectId> {

}
