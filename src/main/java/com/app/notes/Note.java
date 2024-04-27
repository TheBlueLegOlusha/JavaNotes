package com.app.notes;

import com.app.exceptions.BadRequestException;
import com.app.tags.Tag;
import com.app.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.grammars.hql.HqlParser;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(
            name = "notes",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "owner"
            )
    )
    private User owner;

    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "timestamp")
    private String creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "timestamp")
    private String lastUpdateTime;

    @ManyToMany(
    )
    @JoinTable(name = "notes_tags",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name"))
    private Set<Tag> tags;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", owner=" + owner +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", tags=" + tags +
                '}';
    }
}
