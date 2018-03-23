package com.elastic.data.elastic.data.service;


import com.elastic.data.document.Book;
import com.elastic.data.elastic.config.ElasticSearchTestConfig;
import com.elastic.data.elastic.data.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@Import(ElasticSearchTestConfig.class)
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Before
    public void setUp() {
        elasticsearchTemplate.deleteIndex(Book.class);
        elasticsearchTemplate.createIndex(Book.class);
        elasticsearchTemplate.putMapping(Book.class);

        elasticsearchTemplate.refresh(Book.class);
    }

    @Test
    public void should_field_equals_after_save_operation_success() {
        //given
        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        //when
        when(bookRepository.save(book)).thenReturn(book);
        Book testBook = bookService.save(book);
        //then
        assertThat(book.getId()).isEqualTo(testBook.getId());
        assertThat(book.getAuthor()).isEqualTo(testBook.getAuthor());
        assertThat(book.getTitle()).isEqualTo(testBook.getTitle());
        assertThat(book.getReleaseDate()).isEqualTo(testBook.getReleaseDate());
    }

    @Test
    public void should_authors_size_three_after_find_books_by_author() {
        //given
        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        Book book1 = new Book("1002", "Apache Lucene Basics", "Rambabu Posa", "13-MAR-2017");
        Book book2 = new Book("1003", "Apache Solr Basics", "Rambabu Posa", "21-MAR-2017");
        bookService.save(book);
        bookService.save(book1);
        bookService.save(book2);
        //when
        when(bookRepository.findByAuthor("Rambabu", PageRequest.of(0, 10))).thenReturn(new PageImpl<>(Arrays.asList(book, book1, book2)));
        Page<Book> booksByAuthor = bookService.findByAuthor("Rambabu", PageRequest.of(0, 10));
        //then
        assertThat(booksByAuthor.getTotalElements()).isEqualTo(3);
    }
}
