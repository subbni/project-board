package com.example.projectboard.repository;

import com.example.projectboard.config.JpaConfig;
import com.example.projectboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private ArticleRepository articleRepository;
    private ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired  ArticleRepository articleRepository, @Autowired  ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;

    }
    @DisplayName("select test")
    @Test
    void givenTestData_whenSelecting_thenWorksfine() {
        // Given

        // When
        List<Article> articles = articleRepository.findAll();

        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert test")
    @Test
    void givenTestData_whenInserting_thenWorksfind() {
        // Given
       long previousCount = articleRepository.count();

        // When
        Article article = Article.of("new article","new comment","#spring");
        articleRepository.save(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount+1);
    }

    @DisplayName("update test")
    @Test
    void givenTestData_whenUpdating_thenWorksfind() {
        // Given
        Article article = Article.of("new article","new comment","#spring");
        articleRepository.save(article);
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        // When
        Article savedArticle = articleRepository.saveAndFlush(article);

        // Then
       // assertThat(savedArticle.getHashtag()).isEqualTo(updatedHashtag);
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);
    }

    @DisplayName("delete test")
    @Test
    void givenTestData_whenDeleting_thenWorksFind() {
        // Given
        Article article = Article.of("new article","new comment","#spring");
        articleRepository.saveAndFlush(article);
        articleCommentRepository.flush();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        // When
        articleRepository.delete(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount-1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount-deletedCommentsSize);
    }
}
