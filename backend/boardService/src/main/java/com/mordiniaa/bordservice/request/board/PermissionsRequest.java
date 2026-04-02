package com.mordiniaa.bordservice.request.board;

import com.mordiniaa.bordservice.models.board.permissions.BoardPermission;
import com.mordiniaa.bordservice.models.board.permissions.CategoryPermissions;
import com.mordiniaa.bordservice.models.board.permissions.CommentPermission;
import com.mordiniaa.bordservice.models.board.permissions.TaskPermission;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PermissionsRequest {

    @NotNull
    private Set<BoardPermission> boardPermissions;

    @NotNull
    private Set<CategoryPermissions> categoryPermissions;

    @NotNull
    private Set<TaskPermission> taskPermissions;

    @NotNull
    private Set<CommentPermission> commentPermissions;
}
