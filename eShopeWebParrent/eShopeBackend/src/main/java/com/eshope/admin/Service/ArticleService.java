package com.eshope.admin.Service;

import com.eShope.common.entity.Article;
import com.eShope.common.entity.Brand;
import com.eShope.common.entity.User;
import com.eshope.admin.Repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
@Transactional
@Slf4j
public class ArticleService {

    public static final int ARTICLES_PER_PAGE = 5;

    @Autowired
    private ArticleRepository articleRepository;

    public Page<Article> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ARTICLES_PER_PAGE, sort);

        if (keyword != null) {
            return articleRepository.findAll(keyword, pageable);
        }

        return articleRepository.findAll(pageable);
    }

    public boolean isTitleUnique(String title) {

        Article ArticleByTitle = articleRepository.getArticleByTitle(title);
        return ArticleByTitle == null;
    }

    public boolean isAliasUnique(String alias) {

        Article ArticleByTitle = articleRepository.getArticleByAlias(alias);
        return ArticleByTitle == null;
    }
    public void save(Article article, User user) {
        setDefaultAlias(article);
        article.setUpdatedTime(new Date());
        article.setUser(user);

        articleRepository.save(article);
    }

    private void setDefaultAlias(Article article) {
        if (article.getAlias() == null || article.getAlias().isEmpty()) {
            article.setAlias(article.getTitle().replaceAll(" ", "-"));
        }
    }

    public Article getArticleById(Integer id) throws UsernameNotFoundException {
        try {
            return articleRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new UsernameNotFoundException("Could not find any article with ID " + id);
        }
    }

    public void updateArticlePublishStatusById(Integer id, boolean published) throws UsernameNotFoundException {
        if (!articleRepository.existsById(id)) {
            throw new UsernameNotFoundException("Could not find any article with ID " + id);
        }
        articleRepository.updatePublishStatus(id, published);
    }

    public void deleteArticleById(Integer id) throws UsernameNotFoundException {
        if (!articleRepository.existsById(id)) {
            throw new UsernameNotFoundException("Could not find any article with ID " + id);
        }
        articleRepository.deleteById(id);
    }
}
