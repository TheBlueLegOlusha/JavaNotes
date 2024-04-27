package com.app.notes;

import com.app.exceptions.BadRequestException;
import com.app.exceptions.PermissionError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/notes")
@RestController
public class NotesController {
    private NotesService service;

    @Autowired
    public NotesController(NotesService notesService) {
        this.service = notesService;
    }

    @GetMapping
    public List<Note> getAllNotes() {
        return service.getAllNotes();
    }

    @GetMapping(path = "{noteId}")
    public Note getNote(@PathVariable int noteId) {
        return service.getNoteById(noteId);
    }

    @GetMapping(params = {"find_by", "value"})
    public List<Note> getNotesBy(@RequestParam String find_by, @RequestParam String value){
        if (find_by.equals("title")) {
            return service.getNotesByTitle(value);
        }else if (find_by.equals("id")){
            return List.of(service.getNoteById(Integer.parseInt(value)));
        }
        else throw new BadRequestException("find_by=" + find_by + " is not valid argument");
    }

    // TODO owner в будущем будет определяться при помощи ключа сессии
    @PostMapping
    public ResponseEntity<?> addNote(@RequestParam String owner, @RequestBody Note tempNote) throws PermissionError {
        Note note = new Note();
        service.merge(note, tempNote, owner);
        return service.saveNote(note);
    }

    @PutMapping(path = "{noteId}")
    public ResponseEntity<?> updateNote(@RequestParam String owner, @RequestBody Note tempNote, @PathVariable int noteId) throws PermissionError {
        Note existingNote = service.getNoteById(noteId);
        service.merge(existingNote, tempNote, owner);
        return service.saveNote(existingNote);
    }

    @DeleteMapping(path = "{noteId}")
    public void deleteNote(@PathVariable int noteId, @RequestParam String owner) throws PermissionError {
        service.deleteNote(noteId, owner);
    }

    @DeleteMapping
    public void deleteAllNotes(@RequestParam String admin) throws PermissionError {
        service.deleteAllNotes(admin);
    }
}
