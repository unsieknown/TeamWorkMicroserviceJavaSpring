package com.mordiniaa.userservice.events.listeners;

import com.mordiniaa.userservice.events.events.UserDeleteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TeamEventListener {

//    private final TeamAdminService teamAdminService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserDeleteEvent event) {
        // TODO: Implement Logic To Handle User Deletion In Team Service
//        teamAdminService.removeFromTeamByEvent(event.userId());
    }
}
