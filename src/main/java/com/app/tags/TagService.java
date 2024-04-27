package main.java.com.app.tags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class TagService {
    private TagRepository repository;

    @Autowired
    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    public List<Tag> getAllTags(){
        return repository.findAll();
    }

    public void createTag(Tag tag){
        repository.save(tag);
    }

    public void findOrCreateTag(Tag tag){
        System.out.println(tag);
        if (repository.findByName(tag.getName())==null)
            repository.save(tag);
    }

    public void checkTags(Set<Tag> tags){
        tags.forEach(this::findOrCreateTag);
    }
}
