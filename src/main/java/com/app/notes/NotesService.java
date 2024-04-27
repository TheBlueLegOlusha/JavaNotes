package com.app.notes;

import com.app.exceptions.BadRequestException;
import com.app.exceptions.NotFoundException;
import com.app.exceptions.PermissionError;
import com.app.tags.Tag;
import com.app.tags.TagService;
import com.app.users.User;
import com.app.users.UserService;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class NotesService {
    private final TagService tagService;
    private final UserService userService;
    private final NotesRepository repository;

    @Autowired
    public NotesService(NotesRepository notesRepository, TagService tagService, UserService userService) {
        this.repository = notesRepository;
        this.tagService = tagService;
        this.userService = userService;
    }

    public List<Note> getAllNotes() {
        return repository.findAll();
    }

    public List<Note> getNotesByTitle(String title) {
        return repository.findByTitle(title);
    }


    public Note getNoteById(int id) {
        Optional<Note> note = repository.findById(id);
        return note.orElseThrow(() -> new NotFoundException("Note with id " + id + " not exists"));
    }

    public ResponseEntity<?> saveNote(Note note) {
        repository.save(note);
        return ResponseEntity.status(201).body(note);
    }

    public void deleteNote(int noteId, String user) throws PermissionError {
        Note note = repository
                .findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found with id " + noteId));
        if (note.getOwner().equals(userService.getUserByName(user)))
            repository.delete(note);
        else throw new PermissionError("Недостаточно прав");
    }

    public void deleteAllNotes(String user) throws PermissionError {
        if (userService.getUserByName(user).isAdmin())
            repository.deleteAll();
        else throw new PermissionError("Не достаточноо прав!");
    }

    public List<Note> getTaggedNotes(String tagName) {
        return repository.findByTagName(tagName);
    }

    public List<Note> getUntaggedNotes() {
        return repository.findUntagged();
    }

    public void merge(Note note, Note tempNote, String new_owner) throws PermissionError {
        String title = tempNote.getTitle();
        String text = tempNote.getText();
        Set<Tag> tags = tempNote.getTags();
        if (title != null)
            note.setTitle(title);
        if (text != null)
            note.setText(text);
        if (tags != null) {
            this.tagService.checkTags(tags);
            note.setTags(tags);
        }
        if (note.getOwner() == null) {
            note.setOwner(userService.getUserByName(new_owner));
            userService.addNote(new_owner, note);
        } else if (note.getOwner() != userService.getUserByName(new_owner)) {
            throw new PermissionError("Доступ запрещён");
        }
    }
}
