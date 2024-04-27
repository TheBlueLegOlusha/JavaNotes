package main.java.com.app.tags;

import com.app.notes.Note;
import com.app.notes.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/tags")
@RestController
public class TagController {
    private TagService service;
    private NotesService notesService;

    @Autowired
    public TagController(TagService service, NotesService notesService) {
        this.service = service;
        this.notesService = notesService;
    }

    @GetMapping
    public List<Tag> getAllTags(){
        return service.getAllTags();
    }

    @GetMapping(path = "_untagged_")
    private List<Note> getUntaggedNotes(){
        return notesService.getUntaggedNotes();
    }

    @GetMapping(path = "{tagName}")
    private List<Note> getTaggedNotes(@PathVariable String tagName){
        return notesService.getTaggedNotes(tagName);
    }
}
