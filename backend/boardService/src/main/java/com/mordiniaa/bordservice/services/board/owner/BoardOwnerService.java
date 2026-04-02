package com.mordiniaa.bordservice.services.board.owner;

import com.mongodb.client.result.UpdateResult;
import com.mordiniaa.bordservice.clients.user.UserServiceClient;
import com.mordiniaa.bordservice.dto.board.BoardDetailsDto;
import com.mordiniaa.bordservice.dto.user.UserDto;
import com.mordiniaa.bordservice.exceptions.BoardNotFoundException;
import com.mordiniaa.bordservice.exceptions.ResourceNotFoundException;
import com.mordiniaa.bordservice.mappers.BoardMapper;
import com.mordiniaa.bordservice.models.board.Board;
import com.mordiniaa.bordservice.models.board.BoardMember;
import com.mordiniaa.bordservice.models.board.permissions.BoardPermission;
import com.mordiniaa.bordservice.repositories.board.BoardRepository;
import com.mordiniaa.bordservice.repositories.board.aggregation.BoardAggregationRepositoryImpl;
import com.mordiniaa.bordservice.repositories.board.aggregation.returnTypes.BoardFull;
import com.mordiniaa.bordservice.request.board.BoardCreationRequest;
import com.mordiniaa.bordservice.services.board.admin.BoardAdminService;
import com.mordiniaa.bordservice.services.inter.TeamServiceInter;
import com.mordiniaa.bordservice.services.inter.UserServiceInter;
import com.mordiniaa.bordservice.utils.MongoIdUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardOwnerService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final BoardAggregationRepositoryImpl boardAggregationRepositoryImpl;
    private final MongoIdUtils mongoIdUtils;
    private final MongoTemplate mongoTemplate;
    private final BoardAdminService boardAdminService;
    private final UserServiceInter userServiceInter;
    private final TeamServiceInter teamServiceInter;
    private final UserServiceClient userServiceClient;

    public BoardDetailsDto createBoard(UUID userId, BoardCreationRequest boardCreationRequest) {

        userServiceInter.checkUserExistence(userId);

        UUID teamId = boardCreationRequest.getTeamId();

        teamServiceInter.checkTeamAndManagerExistence(teamId, userId);

        Board board = new Board();
        board.setTeamId(teamId);
        board.setBoardName(boardCreationRequest.getBoardName());

        BoardMember ownerMember = boardAdminService.createBoardOwner(userId);

        board.setOwner(ownerMember);

        Board savedBoard = boardRepository.save(board);
        BoardFull aggregatedBoardDocument = boardAggregationRepositoryImpl
                .findBoardWithTasksByUserIdAndBoardIdAndTeamId(userId, savedBoard.getId(), teamId)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        Set<UUID> ids = boardAggregationRepositoryImpl.findBoardUsers(savedBoard.getId());
        Set<UserDto> users = userServiceClient.batchUsers(ids).getUsers();

        UserDto owner = users.stream().filter(user -> Objects.equals(user.getUserId(), userId))
                .findFirst()
                .orElse(null);
        users.remove(owner);

        aggregatedBoardDocument.setOwner(owner);
        aggregatedBoardDocument.setMembers(new ArrayList<>(users));

        return boardMapper.toDetailedDto(aggregatedBoardDocument);
    }

    public void addUserToBoard(UUID boardOwner, UUID userId, String bId) {

        userServiceInter.checkUsersExistence(Set.of(boardOwner, userId));
        ObjectId boardId = mongoIdUtils.getObjectId(bId);

        Board board = boardAggregationRepositoryImpl.findFullBoardByIdAndOwner(boardId, boardOwner)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        UUID teamId = board.getTeamId();

        teamServiceInter.checkUserInTeamExistence(teamId, userId);

        BoardMember newMember = new BoardMember(userId);
        newMember.setBoardPermissions(Set.of(BoardPermission.VIEW_BOARD));

        board.addMember(newMember);
        boardRepository.save(board);
    }

    public void removeUserFromBoard(UUID boardOwner, UUID userId, String bId) {

        userServiceInter.checkUserExistence(userId);
        ObjectId boardId = mongoIdUtils.getObjectId(bId);

        Board board = boardAggregationRepositoryImpl.findFullBoardByIdAndOwner(boardId, boardOwner)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));
        board.removeMember(userId);
        boardRepository.save(board);
    }

    public void deleteBoard(UUID boardOwner, String bId) {

        userServiceInter.checkUserExistence(boardOwner);
        ObjectId boardId = mongoIdUtils.getObjectId(bId);

        Update update = new Update()
                .set("archived", true)
                .set("deleted", true);
        Query updateQuery = Query.query(
                new Criteria().andOperator(
                        Criteria.where("_id").is(boardId),
                        Criteria.where("owner.userId").is(boardOwner)
                )
        );

        UpdateResult result = mongoTemplate.updateFirst(updateQuery, update, Board.class);
        if (result.getModifiedCount() == 0)
            throw new ResourceNotFoundException("Resource not found");
    }
}
