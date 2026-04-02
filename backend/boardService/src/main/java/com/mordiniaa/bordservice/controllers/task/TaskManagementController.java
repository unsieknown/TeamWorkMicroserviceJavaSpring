package com.mordiniaa.bordservice.controllers.task;

import com.mordiniaa.bordservice.dto.task.TaskDetailsDTO;
import com.mordiniaa.bordservice.request.task.AssignUsersRequest;
import com.mordiniaa.bordservice.request.task.PatchTaskDataRequest;
import com.mordiniaa.bordservice.response.APIResponse;
import com.mordiniaa.bordservice.services.task.TaskManagementService;
import com.mordiniaa.bordservice.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks/{taskId}/management")
public class TaskManagementController {

    private final AuthUtils authUtils;
    private final TaskManagementService taskManagementService;

    @PatchMapping
    public ResponseEntity<APIResponse<TaskDetailsDTO>> updateTask(
            @PathVariable String taskId,
            @RequestParam("b") String boardId,
            @RequestBody PatchTaskDataRequest patchTaskDataRequest
    ) {

        UUID userId = authUtils.authenticatedUserId();
        TaskDetailsDTO dto = taskManagementService.updateTask(userId, boardId, taskId, patchTaskDataRequest);

        return ResponseEntity.ok(
                new APIResponse<>(
                        "Updated Successfully",
                        dto
                )
        );
    }

    @PutMapping("/user/add")
    public ResponseEntity<APIResponse<TaskDetailsDTO>> assignUsersToTask(
            @PathVariable String taskId,
            @RequestParam("b") String boardId,
            @Valid @RequestBody AssignUsersRequest assignUsersRequest
    ) {

        UUID userId = authUtils.authenticatedUserId();
        TaskDetailsDTO dto = taskManagementService.assignUsersToTask(userId, assignUsersRequest, boardId, taskId);
        return ResponseEntity.ok(
                new APIResponse<>(
                        "Successfully Assigned",
                        dto
                )
        );
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<Void> removeUserFromTask(
            @PathVariable String taskId,
            @RequestParam("b") String boardId,
            @RequestParam("u") UUID userToDeleteId
    ) {

        UUID userId = authUtils.authenticatedUserId();
        taskManagementService.removeUserFromTask(userId, userToDeleteId, boardId, taskId);

        return ResponseEntity.noContent().build();
    }
}
