package com.mordiniaa.userservice.events.listeners;

import com.mordiniaa.userservice.events.events.*;
import com.mordiniaa.userservice.messaging.rabbit.publish.RabbitMQPublisher;
import com.mordiniaa.userservice.messaging.rabbit.publish.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListenerForRabbitForward {

    private final RabbitMQPublisher rabbitMQPublisher;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserDefaultImageEvent event) {
        rabbitMQPublisher.publishUserDefaultImageMessage(
                new UserMessage(event.userid())
        );
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserCreatedEvent event) {
        rabbitMQPublisher.publishUserCreatedMessage(
                new UserMessage(event.userId())
        );
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserDeleteEvent event) {
        rabbitMQPublisher.publishUserDeletedMessage(
                new UserMessage(event.userId())
        );
    }
}
