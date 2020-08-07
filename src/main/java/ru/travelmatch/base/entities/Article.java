package ru.travelmatch.base.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Farida Gareeva
 * Created 08/06/2020
 * v1.0
 * Сущность для хранения статей
 */

@Entity
//@Data //"java.lang.StackOverflowError" with this annotation - changed to getter setter
@Getter
@Setter
@NoArgsConstructor
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "text", length = 10000)
    private String text;

    @JsonManagedReference
    @ManyToOne (optional = false)
    private ArticleCategory category;

    @JsonManagedReference
    @ManyToOne
    private User author;

    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @ManyToOne
    private City city;

    @Column(name = "main_picture_url")
    private String mainPictureURL;

    @Formula("(select count(a.user_id) from article_likes_ratings a where a.like_dislike=1 and a.article_id = id)")
    private Long countLikes;

    @Formula("(select count(a.user_id) from article_likes_ratings a where a.like_dislike=-1 and a.article_id = id)")
    private Long countDislikes;

    @Formula("(select avg(a.rating) from article_likes_ratings a where a.rating>=1 and a.rating<=5 and a.article_id = id)")
    private Double rating;

    @ManyToOne
    private Language language;

    @JsonBackReference
    @OneToMany(mappedBy = "article",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments;

    @OneToMany(mappedBy = "article",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ArticleLikeRating> likes;

    @ManyToMany
    @JoinTable(name = "articles_tags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

}