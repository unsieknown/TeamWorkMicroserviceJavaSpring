package com.mordiniaa.bordservice.services.board.owner;

import com.mongodb.client.result.UpdateResult;
import com.mordiniaa.bordservice.exceptions.BoardNotFoundException;
import com.mordiniaa.bordservice.exceptions.ResourceNotFoundException;
import com.mordiniaa.bordservice.models.board.Board;
import com.mordiniaa.bordservice.models.board.BoardMember;
import com.mordiniaa.bordservice.repositories.board.BoardRepository;
import com.mordiniaa.bordservice.repositories.board.aggregation.BoardAggregationRepository;
import com.mordiniaa.bordservice.request.board.PermissionsRequest;
import com.mordiniaa.bordservice.services.inter.UserServiceInter;
import com.mordiniaa.bordservice.utils.BoardUtils;
import com.mordiniaa.bordservice.utils.MongoIdUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardOwnerManagementService {

    private final MongoIdUtils mongoIdUtils;
    private final BoardRepository boardRepository;
    private final BoardAggregationRepository boardAggregationRepository;
    private final BoardUtils boardUtils;
    private final MongoTemplate mongoTemplate;
    private final UserServiceInter userServiceInter;

    public void changeBoardMemberPermissions(UUID ownerId, String bId, UUID userId, PermissionsRequest permissionsRequest) {

        userServiceInter.checkUsersExistence(Set.of(ownerId, userId));

        ObjectId boardId = mongoIdUtils.getObjectId(bId);
        Board board = boardAggregationRepository.findFullBoardByIdAndOwnerAndExistingMember(boardId, ownerId, userId)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        BoardMember member = boardUtils.getBoardMember(board, userId);

        member.setBoardPermissions(permissionsRequest.getBoardPermissions());
        member.setCategoryPermissions(permissionsRequest.getCategoryPermissions());
        member.setTaskPermissions(permissionsRequest.getTaskPermissions());
        member.setCommentPermissions(permissionsRequest.getCommentPermissions());
        boardRepository.save(board);
    }

    public void archiveBoard(UUID ownerId, String bId) {
        setBoardArchivedStatus(ownerId, bId, true);
    }

    public void restoreBoard(UUID ownerId, String bId) {
        setBoardArchivedStatus(ownerId, bId, false);
    }

    private void setBoardArchivedStatus(UUID ownerId, String bId, boolean status) {

        userServiceInter.checkUserExistence(ownerId);

        ObjectId boardId = mongoIdUtils.getObjectId(bId);

        Query updateQuery = Query.query(
                Criteria.where("_id").is(boardId)
                        .and("owner.userId").is(ownerId)
                        .and("deleted").is(false)
                        .and("archived").ne(status)
        );
        Update update = new Update()
                .set("archived", status);

        UpdateResult result = mongoTemplate.updateFirst(updateQuery, update, Board.class);
        if (result.getModifiedCount() == 0)
            throw new ResourceNotFoundException("Resource Not Found");
    }
}
