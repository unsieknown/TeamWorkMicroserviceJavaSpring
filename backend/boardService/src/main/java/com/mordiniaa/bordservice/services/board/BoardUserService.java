package com.mordiniaa.bordservice.services.board;

import com.mordiniaa.bordservice.clients.user.UserServiceClient;
import com.mordiniaa.bordservice.dto.board.BoardDetailsDto;
import com.mordiniaa.bordservice.dto.board.BoardShortDto;
import com.mordiniaa.bordservice.dto.user.UserDto;
import com.mordiniaa.bordservice.exceptions.AccessDeniedException;
import com.mordiniaa.bordservice.exceptions.BoardNotFoundException;
import com.mordiniaa.bordservice.mappers.BoardMapper;
import com.mordiniaa.bordservice.models.board.Board;
import com.mordiniaa.bordservice.models.board.BoardMember;
import com.mordiniaa.bordservice.repositories.board.aggregation.BoardAggregationRepositoryImpl;
import com.mordiniaa.bordservice.repositories.board.aggregation.returnTypes.BoardFull;
import com.mordiniaa.bordservice.repositories.board.aggregation.returnTypes.BoardMembersOnly;
import com.mordiniaa.bordservice.services.inter.UserServiceInter;
import com.mordiniaa.bordservice.utils.BoardUtils;
import com.mordiniaa.bordservice.utils.MongoIdUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardUserService {

    private final BoardAggregationRepositoryImpl boardAggregationRepositoryImpl;
    private final BoardMapper boardMapper;
    private final MongoIdUtils mongoIdUtils;
    private final BoardUtils boardUtils;
    private final UserServiceClient userServiceClient;
    private final UserServiceInter userServiceInter;

    public List<BoardShortDto> getBoardListForUser(UUID userId, UUID teamId) {

        userServiceInter.checkUserExistence(userId);

        return boardAggregationRepositoryImpl.findAllBoardsForUserByUserIdAndTeamId(userId, teamId)
                .stream()
                .sorted(Comparator.comparing(Board::getUpdatedAt).reversed())
                .map(boardMapper::toShortDto)
                .toList();
    }

    public BoardDetailsDto getBoardDetails(UUID userId, String bId, UUID teamId) {

        userServiceInter.checkUserExistence(userId);

        ObjectId boardId = mongoIdUtils.getObjectId(bId);

        BoardMembersOnly b = boardAggregationRepositoryImpl.findBoardMembers(boardId, userId, teamId)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        BoardMember currentMember = boardUtils.getBoardMember(b, userId);
        if (!currentMember.canViewBoard())
            throw new AccessDeniedException("You do not have permission to perform this operation");

        BoardFull board = boardAggregationRepositoryImpl.findBoardWithTasksByUserIdAndBoardIdAndTeamId(userId, boardId, teamId)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        Set<UUID> ids = boardAggregationRepositoryImpl.findBoardUsers(boardId);
        Set<UserDto> users = userServiceClient.batchUsers(ids).getUsers();

        UserDto owner = users.stream().filter(user -> Objects.equals(user.getUserId(), userId))
                .findFirst()
                .orElse(null);
        users.remove(owner);

        board.setOwner(owner);
        board.setMembers(new ArrayList<>(users));

        return boardMapper.toDetailedDto(board);
    }
}
