package com.eShope.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "articles")
@Getter
@Setter
public class Article  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 256)
    @NotBlank(message="Title must not be blank")
    @Size(min=5,message="Title must be at least 5 characters long")
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false, length = 500)
    @NotBlank(message="Alias must not be blank")
    @Size(min=5, message="Alias must be at least 5 characters long")
    private String alias;

    @Enumerated(EnumType.ORDINAL)
    private ArticleType type;

    @Column(name = "updated_time")
    private Date updatedTime;

    private boolean published;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Article() {
    }
    public Article(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Article(Integer id, String title, ArticleType type, Date updatedTime, boolean published, User user) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.updatedTime = updatedTime;
        this.published = published;
        this.user = user;
    }

    public Article(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ArticleType getType() {
        return type;
    }

    public void setType(ArticleType type) {
        this.type = type;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Article [title=" + title + ", type=" + type + "]";
    }


}