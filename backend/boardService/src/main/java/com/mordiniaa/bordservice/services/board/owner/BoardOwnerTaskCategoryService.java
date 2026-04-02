package com.mordiniaa.bordservice.services.board.owner;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mordiniaa.bordservice.clients.user.UserServiceClient;
import com.mordiniaa.bordservice.dto.board.BoardDetailsDto;
import com.mordiniaa.bordservice.dto.user.UserDto;
import com.mordiniaa.bordservice.exceptions.*;
import com.mordiniaa.bordservice.mappers.BoardMapper;
import com.mordiniaa.bordservice.models.board.Board;
import com.mordiniaa.bordservice.models.board.TaskCategory;
import com.mordiniaa.bordservice.models.task.Task;
import com.mordiniaa.bordservice.repositories.board.BoardRepository;
import com.mordiniaa.bordservice.repositories.board.aggregation.BoardAggregationRepository;
import com.mordiniaa.bordservice.repositories.board.aggregation.returnTypes.BoardFull;
import com.mordiniaa.bordservice.request.board.TaskCategoryRequest;
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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BoardOwnerTaskCategoryService {

    private final BoardAggregationRepository boardAggregationRepository;
    private final MongoIdUtils mongoIdUtils;
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final MongoTemplate mongoTemplate;
    private final UserServiceInter userServiceInter;
    private final UserServiceClient userServiceClient;

    public BoardDetailsDto createTaskCategory(UUID boardOwner, String bId, TaskCategoryRequest request) {

        userServiceInter.checkUserExistence(boardOwner);

        ObjectId boardId = mongoIdUtils.getObjectId(bId);

        Board board = boardAggregationRepository.findFullBoardByIdAndOwner(boardId, boardOwner)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        String categoryName = request.getNewCategoryName().trim();
        int newPosition = board.getNextPosition();

        TaskCategory newCategory = new TaskCategory();
        newCategory.setPosition(newPosition);
        newCategory.setCategoryName(categoryName);

        Query query = Query.query(
                Criteria.where("_id").is(boardId)
                        .and("owner.userId").is(boardOwner)
                        .and("taskCategories.categoryName").ne(categoryName)
        );

        Update update = new Update()
                .push("taskCategories", newCategory)
                .inc("highestTaskCategoryPosition", 1);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Board.class);
        if (result.getModifiedCount() == 0)
            throw new ResourceNotFoundException("Resource not found");

        BoardFull updatedBoard = boardAggregationRepository
                .findBoardWithTasksByUserIdAndBoardIdAndTeamId(boardOwner, boardId, board.getTeamId())
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        Set<UUID> ids = boardAggregationRepository.findBoardUsers(boardId);
        Set<UserDto> users = userServiceClient.batchUsers(ids).getUsers();

        UserDto owner = users.stream().filter(user -> Objects.equals(user.getUserId(), boardOwner))
                .findFirst()
                .orElse(null);
        users.remove(owner);

        updatedBoard.setOwner(owner);
        updatedBoard.setMembers(new ArrayList<>(users));

        return boardMapper.toDetailedDto(updatedBoard);
    }

    public BoardDetailsDto renameTaskCategory(UUID boardOwner, String bId, UUID teamId, TaskCategoryRequest request) {

        userServiceInter.checkUserExistence(boardOwner);

        ObjectId boardId = mongoIdUtils.getObjectId(bId);

        String newCategoryName = request.getNewCategoryName().trim();

        String oldCatName = request.getExistingCategoryName();
        if (oldCatName == null || oldCatName.isBlank())
            throw new BadRequestException("Existing category name cannot be null or blank");
        oldCatName = oldCatName.trim();

        Query query = Query.query(
                new Criteria().andOperator(
                        Criteria.where("_id").is(boardId),
                        Criteria.where("teamId").is(teamId),
                        Criteria.where("owner.userId").is(boardOwner),
                        Criteria.where("taskCategories.categoryName").is(oldCatName),
                        Criteria.where("taskCategories.categoryName").ne(newCategoryName)
                )
        );

        Update update = new Update()
                .set("taskCategories.$[cat].categoryName", newCategoryName)
                .filterArray(Criteria.where("cat.categoryName").is(oldCatName));

        UpdateResult result = mongoTemplate.updateFirst(query, update, Board.class);
        if (result.getModifiedCount() == 0)
            throw new ResourceNotFoundException("Resource not found");

        BoardFull updatedBoard = boardAggregationRepository
                .findBoardWithTasksByUserIdAndBoardIdAndTeamId(boardOwner, boardId, teamId)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        Set<UUID> ids = boardAggregationRepository.findBoardUsers(boardId);
        Set<UserDto> users = userServiceClient.batchUsers(ids).getUsers();

        UserDto owner = users.stream().filter(user -> Objects.equals(user.getUserId(), boardOwner))
                .findFirst()
                .orElse(null);
        users.remove(owner);

        updatedBoard.setOwner(owner);
        updatedBoard.setMembers(new ArrayList<>(users));

        return boardMapper.toDetailedDto(updatedBoard);
    }

    public BoardDetailsDto reorderTaskCategories(UUID boardOwner, String bId, UUID teamId, TaskCategoryRequest request, int newPosition) {

        userServiceInter.checkUserExistence(boardOwner);

        ObjectId boardId = mongoIdUtils.getObjectId(bId);

        String catName = request.getExistingCategoryName();
        if (catName == null || catName.isBlank())
            throw new BadRequestException("Existing category name cannot be null or blank");
        catName = catName.trim();

        Board board = boardAggregationRepository.findFullBoardByIdAndOwner(boardId, boardOwner)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        if (newPosition >= board.getNextPosition() || newPosition < 0)
            throw new BadRequestException("Position cannot be less than 0 and gather than last position");

        String categoryName = catName;
        TaskCategory category = board.getTaskCategories()
                .stream()
                .filter(tC -> tC.getCategoryName().equals(categoryName))
                .findFirst().orElse(null);

        if (category == null)
            throw new ResourceNotFoundException("Category not found");

        int currentPosition = category.getPosition();
        if (currentPosition == newPosition)
            throw new UnsupportedOperationException("Category is already at the specified position");

        Stream<TaskCategory> categoryStream = board.getTaskCategories().stream();
        if (newPosition > currentPosition)
            categoryStream.filter(tc -> tc.getPosition() > currentPosition && tc.getPosition() <= newPosition)
                    .forEach(TaskCategory::lowerPosition);
        else
            categoryStream.filter(tc -> tc.getPosition() < currentPosition && tc.getPosition() >= newPosition)
                    .forEach(TaskCategory::higherPosition);
        category.setPosition(newPosition);

        boardRepository.save(board);
        BoardFull updatedBoard = boardAggregationRepository
                .findBoardWithTasksByUserIdAndBoardIdAndTeamId(boardOwner, boardId, teamId)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        Set<UUID> ids = boardAggregationRepository.findBoardUsers(boardId);
        Set<UserDto> users = userServiceClient.batchUsers(ids).getUsers();

        UserDto owner = users.stream().filter(user -> Objects.equals(user.getUserId(), boardOwner))
                .findFirst()
                .orElse(null);
        users.remove(owner);

        updatedBoard.setOwner(owner);
        updatedBoard.setMembers(new ArrayList<>(users));

        return boardMapper.toDetailedDto(updatedBoard);
    }

    public void deleteTaskCategory(UUID boardOwner, String bId, UUID teamId, TaskCategoryRequest request) {

        userServiceInter.checkUserExistence(boardOwner);

        ObjectId boardId = mongoIdUtils.getObjectId(bId);

        String catName = request.getExistingCategoryName();
        if (catName == null || catName.isBlank())
            throw new BadRequestException("Existing category name cannot be null or blank");
        catName = catName.trim();

        Board board = boardRepository.findBoardByIdAndOwner_UserIdAndTeamId(boardId, boardOwner, teamId)
                .orElseThrow(() -> new BoardNotFoundException("Board Not Found"));

        String categoryName = catName;
        TaskCategory taskCategory = board.getTaskCategories().stream()
                .filter(tC -> tC.getCategoryName().equals(categoryName))
                .findFirst()
                .orElseThrow(() -> new TaskCategoryNotFoundException("Task Category Not Found"));

        int currentPosition = taskCategory.getPosition();
        board.getTaskCategories().stream().filter(tc -> tc.getPosition() > currentPosition)
                .forEach(TaskCategory::lowerPosition);

        Set<ObjectId> tasksIds = taskCategory.getTasks();
        board.removeTaskCategory(taskCategory);

        Query tasksQuery = Query.query(
                Criteria.where("_id").in(tasksIds)
        );

        DeleteResult result = mongoTemplate.remove(tasksQuery, Task.class);
        if (result.getDeletedCount() != tasksIds.size())
            throw new UnexpectedException("Unknown Error Occurred");

        boardRepository.save(board);
    }
}
