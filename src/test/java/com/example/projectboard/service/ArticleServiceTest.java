package com.example.projectboard.service;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.type.SearchType;
import com.example.projectboard.dto.ArticleDto;
import com.example.projectboard.dto.ArticleUpdateDto;
import com.example.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("business logic - 게시글")
@ExtendWith(MockitoExtension.class) // test시 spring boot를 띄우지 않고, mockito를 사용하여 필요한 의존성을 주입받아 테스트 함
class ArticleServiceTest {

    @InjectMocks private ArticleService sut;
    @Mock private ArticleRepository articleRepository;

    /*
    검색
    각 게시글 페이지로 이동
    페이지네이션
    홈 버튼 -> 게시판 페이지로 리다이렉션
    정렬 기능
     */

    @DisplayName("게시글을 검색하면 게시글 리스트를 리턴한다.")
    @Test
    void givenSearchParams_whenSearchingArticles_thenReturnsArticleList() {
        // Given

        // When
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search-keyword"); // 제목, 본문, ID, 닉네임, 해시태그

        // Then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글을 요청하면 게시글을 리턴한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        // Given

        // When
        ArticleDto article = sut.searchArticle(1L);

        // Then
        assertThat(article).isNull();
    }

    @DisplayName("게시글 정보를 입력하면 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenRequestingToSaveAnArticle_thenSavesTheArticleWell() {
        // Given
        given(articleRepository.save(any(Article.class))).willReturn(null);

        // When
        sut.saveArticle(ArticleDto.of(LocalDateTime.now(), "Subbni","title","content","#Spring"));

        // Then
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 ID와 수정 정보를 입력하면 게시글을 수정한다.")
    @Test
    void givenArticleIdAndModifiedInfo_whenRequestingToUpdateArticle_thenUpdateTheArticle() {
        // Given
        given(articleRepository.save(any(Article.class))).willReturn(null);

        // When
        sut.updateArticle(1L, ArticleUpdateDto.of("title","updated content","#Spring"));

        // Then
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글 ID를 입력하면 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenRequestingToDeleteArticle_thenDeleteTheArticle() {
        // Given
        willDoNothing().given(articleRepository).delete(any(Article.class));

        // When
        sut.deleteArticle(1L);

        // Then
        then(articleRepository).should().delete(any(Article.class));
    }

}