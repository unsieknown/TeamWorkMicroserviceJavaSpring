package com.mordiniaa.bordservice.controllers.task;

import com.mordiniaa.bordservice.dto.task.TaskDetailsDTO;
import com.mordiniaa.bordservice.dto.task.TaskShortDto;
import com.mordiniaa.bordservice.request.task.UpdateTaskPositionRequest;
import com.mordiniaa.bordservice.request.task.UploadCommentRequest;
import com.mordiniaa.bordservice.response.APIResponse;
import com.mordiniaa.bordservice.services.task.TaskActivityService;
import com.mordiniaa.bordservice.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks/{taskId}/activity")
public class TaskActivityController {

    private final AuthUtils authUtils;
    private final TaskActivityService taskActivityService;

    @PutMapping("/position")
    public ResponseEntity<APIResponse<TaskShortDto>> changeTaskPosition(
            @PathVariable String taskId,
            @RequestParam("b") String boardId,
            @Valid @RequestBody UpdateTaskPositionRequest updateTaskPositionRequest
            ) {

        UUID userId = authUtils.authenticatedUserId();

        TaskShortDto dto = taskActivityService.changeTaskPosition(userId, boardId, taskId, updateTaskPositionRequest);
        return ResponseEntity.ok(
                new APIResponse<>(
                        "Updated Successfully",
                        dto
                )
        );
    }

    @PostMapping("/comment")
    public ResponseEntity<APIResponse<TaskDetailsDTO>> writeComment(
            @PathVariable String taskId,
            @RequestParam("b") String boardId,
            @Valid @RequestBody UploadCommentRequest updateTaskPositionRequest
    ) {

        UUID userId = authUtils.authenticatedUserId();

        TaskDetailsDTO dto = taskActivityService.writeComment(userId, boardId, taskId, updateTaskPositionRequest);
        return ResponseEntity.ok(
                new APIResponse<>(
                        "Commented Successfully",
                        dto
                )
        );
    }

    @PutMapping("/comment")
    public ResponseEntity<APIResponse<TaskDetailsDTO>> updateComment(
            @PathVariable String taskId,
            @RequestParam("b") String boardId,
            @Valid @RequestBody UploadCommentRequest updateTaskPositionRequest
    ) {

        UUID userId = authUtils.authenticatedUserId();

        TaskDetailsDTO dto = taskActivityService.updateComment(userId, boardId, taskId, updateTaskPositionRequest);
        return ResponseEntity.ok(
                new APIResponse<>(
                        "Updated Successfully",
                        dto
                )
        );
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<APIResponse<TaskDetailsDTO>> deleteComment(
            @PathVariable String taskId,
            @PathVariable UUID commentId,
            @RequestParam("b") String boardId
    ) {

        UUID userId = authUtils.authenticatedUserId();

        TaskDetailsDTO dto = taskActivityService.deleteComment(userId, boardId, taskId, commentId);
        return ResponseEntity.ok(
                new APIResponse<>(
                        "Deleted Successfully",
                        dto
                )
        );
    }
}
