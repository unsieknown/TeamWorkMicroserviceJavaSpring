package com.mordiniaa.bordservice.controllers.task;

import com.mordiniaa.bordservice.dto.task.TaskDetailsDTO;
import com.mordiniaa.bordservice.dto.task.TaskShortDto;
import com.mordiniaa.bordservice.request.task.CreateTaskRequest;
import com.mordiniaa.bordservice.response.APIResponse;
import com.mordiniaa.bordservice.services.task.TaskService;
import com.mordiniaa.bordservice.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final AuthUtils authUtils;
    private final TaskService taskService;

    @GetMapping("/{taskId}")
    public ResponseEntity<APIResponse<TaskDetailsDTO>> getTaskDetails(
            @RequestParam("b") String boardId,
            @PathVariable String taskId
    ) {
        UUID userId = authUtils.authenticatedUserId();
        TaskDetailsDTO dto = taskService.getTaskDetailsById(userId, boardId, taskId);

        return ResponseEntity.ok(
                new APIResponse<>(
                        "Successfully Created",
                        dto
                )
        );
    }

    @PostMapping
    public ResponseEntity<APIResponse<TaskShortDto>> createTask(
            @RequestParam("b") String boardId,
            @RequestParam("cn") String categoryName,
            @Valid @RequestBody CreateTaskRequest createTaskRequest
    ) {

        UUID userId = authUtils.authenticatedUserId();
        TaskShortDto dto = taskService.createTask(userId, boardId, categoryName, createTaskRequest);

        return ResponseEntity.ok(
                new APIResponse<>(
                        "Successfully Created",
                        dto
                )
        );
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<APIResponse<Void>> deleteTaskById(
            @RequestParam("b") String boardId,
            @PathVariable String taskId
    ) {
        UUID userId = authUtils.authenticatedUserId();
        taskService.deleteTaskFromBoard(userId, boardId, taskId);

        return ResponseEntity.ok(
                new APIResponse<>(
                        "Successfully Created",
                        null
                )
        );
    }
}
