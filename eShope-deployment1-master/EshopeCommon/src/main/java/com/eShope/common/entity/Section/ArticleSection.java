package com.eShope.common.entity.Section;

import com.eShope.common.entity.Article;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "sections_articles")
public class ArticleSection  {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "article_order")
    private int articleOrder;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public int getArticleOrder() {
        return articleOrder;
    }

    public void setArticleOrder(int articleOrder) {
        this.articleOrder = articleOrder;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

}