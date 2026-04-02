package com.mordiniaa.bordservice.utils;

import com.mordiniaa.bordservice.exceptions.BadRequestException;
import com.mordiniaa.bordservice.models.board.BoardMember;
import com.mordiniaa.bordservice.models.board.BoardMembers;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BoardUtils {

    public BoardMember getBoardMember(BoardMembers board, UUID userId) {
        if (board.getOwner().getUserId().equals(userId)) {
            return board.getOwner();
        } else {
            return board.getMembers()
                    .stream()
                    .filter(bm -> bm.getUserId().equals(userId))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException("Board Member Not Found"));
        }
    }
}
