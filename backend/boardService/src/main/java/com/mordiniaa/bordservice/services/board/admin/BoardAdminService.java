package com.mordiniaa.bordservice.services.board.admin;

import com.mongodb.BasicDBObject;
import com.mordiniaa.bordservice.exceptions.BoardNotFoundException;
import com.mordiniaa.bordservice.messaging.rabbit.consume.UserMessage;
import com.mordiniaa.bordservice.models.board.Board;
import com.mordiniaa.bordservice.models.board.BoardMember;
import com.mordiniaa.bordservice.models.board.permissions.BoardPermission;
import com.mordiniaa.bordservice.models.board.permissions.CategoryPermissions;
import com.mordiniaa.bordservice.models.board.permissions.CommentPermission;
import com.mordiniaa.bordservice.models.board.permissions.TaskPermission;
import com.mordiniaa.bordservice.repositories.board.BoardRepository;
import com.mordiniaa.bordservice.services.inter.TeamServiceInter;
import com.mordiniaa.bordservice.services.inter.UserServiceInter;
import com.mordiniaa.bordservice.utils.MongoIdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardAdminService {

    private final MongoTemplate mongoTemplate;
    private final BoardRepository boardRepository;
    private final MongoIdUtils mongoIdUtils;
    private final UserServiceInter userServiceInter;
    private final TeamServiceInter teamServiceInter;

    /**
     * Method Called By RabbitMQ
     *
     * @param message RabbitMQ Message With User Id And User Role
     */
    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.userDelete}")
    public void handleUserDeletion(UserMessage message) {

        if (message.appRole().equals(UserMessage.AppRole.ROLE_MANAGER))
            removeBoardOwner(message.userId());
        else
            removeUserFromBoards(message.userId());
    }

    private void removeBoardOwner(UUID userId) {

        Query query = Query.query(Criteria.where("owner.userId").is(userId));
        Update update = new Update().unset("owner");

        mongoTemplate.updateMulti(query, update, Board.class);
    }

    private void removeUserFromBoards(UUID userId) {

        Query query = Query.query(Criteria.where("members.userId").is(userId));
        Update update = new Update().pull("members",
                new BasicDBObject("userId", userId));
        mongoTemplate.updateMulti(query, update, Board.class);
    }

    @Transactional
    public void setBoardOwner(String bId, UUID userId, UUID teamId) {

        ObjectId boardId = mongoIdUtils.getObjectId(bId);

        userServiceInter.checkUserExistence(userId);

        teamServiceInter.checkTeamAndManagerExistence(teamId, userId);

        Board board = boardRepository.findByIdAndTeamId(boardId, teamId)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        BoardMember ownerMember = createBoardOwner(userId);
        board.setOwner(ownerMember);
        boardRepository.save(board);
    }

    public BoardMember createBoardOwner(UUID userId) {
        BoardMember ownerMember = new BoardMember(userId);
        ownerMember.setBoardPermissions(Set.of(BoardPermission.values()));
        ownerMember.setCategoryPermissions(Set.of(CategoryPermissions.values()));
        ownerMember.setTaskPermissions(Set.of(TaskPermission.values()));
        ownerMember.setCommentPermissions(Set.of(CommentPermission.values()));
        return ownerMember;
    }
}
