package com.mordiniaa.bordservice.controllers.board;

import com.mordiniaa.bordservice.response.APIResponse;
import com.mordiniaa.bordservice.services.board.admin.BoardAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/admin/{boardId}")
public class BoardAdminController {

    private final BoardAdminService boardAdminService;

    @PutMapping
    public ResponseEntity<APIResponse<Void>> setBoardOwner(
            @RequestParam(name = "u") UUID userId,
            @RequestParam(name = "t") UUID teamId,
            @PathVariable String boardId
    ) {

        boardAdminService.setBoardOwner(boardId, userId, teamId);
        return ResponseEntity.ok(
                new APIResponse<>(
                        "Updated Successfully",
                        null
                )
        );
    }
}
