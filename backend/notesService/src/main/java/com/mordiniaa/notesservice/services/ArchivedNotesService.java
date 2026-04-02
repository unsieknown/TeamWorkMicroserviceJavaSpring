package com.mordiniaa.notesservice.services;

import com.mongodb.client.result.UpdateResult;
import com.mordiniaa.notesservice.dto.NoteDto;
import com.mordiniaa.notesservice.exceptions.BadRequestException;
import com.mordiniaa.notesservice.exceptions.NoteNotFoundException;
import com.mordiniaa.notesservice.exceptions.ResourceNotFoundException;
import com.mordiniaa.notesservice.mappers.NoteMapper;
import com.mordiniaa.notesservice.models.Note;
import com.mordiniaa.notesservice.repositories.NotesRepository;
import com.mordiniaa.notesservice.utils.PageResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArchivedNotesService {

    private final NotesRepository notesRepository;
    private final NoteMapper noteMapper;
    private final MongoTemplate mongoTemplate;

    public PageResult<List<NoteDto>> fetchAllArchivedNotes(UUID ownerId, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Note> page = notesRepository.findAllByOwnerIdAndArchived(ownerId, true, pageable);

        PageResult<List<NoteDto>> result = new PageResult<>();

        result.setData(page.map(noteMapper::toDto).stream().toList());
        result.setUpPage(page);

        return result;
    }

    public void switchArchivedNoteForUser(UUID ownerId, String noteId) {

        if (!ObjectId.isValid(noteId)) {
            throw new BadRequestException("Invalid Note Id");
        }

        Query query = Query.query(
                Criteria.where("_id").is(new ObjectId(noteId))
                        .and("ownerId").is(ownerId)
        );

        Note note = mongoTemplate.findOne(query, Note.class);
        if (note == null) {
            throw new NoteNotFoundException("Note Not Found");
        }

        Update update = new Update()
                .set("archived", !note.isArchived());

        UpdateResult result = mongoTemplate.updateFirst(query, update, Note.class);

        if (result.getModifiedCount() != 1) {
            throw new ResourceNotFoundException("Resource not found");
        }
    }
}
