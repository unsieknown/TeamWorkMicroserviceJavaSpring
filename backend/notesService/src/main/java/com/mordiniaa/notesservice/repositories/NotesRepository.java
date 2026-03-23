package com.mordiniaa.notesservice.repositories;

import com.mordiniaa.notesservice.models.Note;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotesRepository extends MongoRepository<Note, ObjectId> {

    Page<Note> findAllByOwnerIdAndArchivedFalse(UUID ownerId, Pageable pageable, TextCriteria textCriteria);

    Optional<Note> findNoteByIdAndOwnerId(ObjectId id, UUID ownerId);

    long deleteByIdAndOwnerId(ObjectId id, UUID ownerId);

    void deleteAllByOwnerId(UUID ownerId);

    Page<Note> findAllByOwnerIdAndArchived(UUID ownerId, boolean archived, Pageable pageable);
}
