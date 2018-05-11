package com.elastic.data.boot;

import com.elastic.data.document.Book;
import com.elastic.data.elastic.data.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;


@Component
public class ApplicationRunner implements CommandLineRunner {

    private final BookService bookService;
    private final ElasticsearchOperations elasticsearchOperations;

    public ApplicationRunner(BookService bookService, ElasticsearchOperations elasticsearchOperations) {
        this.bookService = bookService;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public void run(String... args) {
        elasticsearchOperations.deleteIndex(Book.class);
        elasticsearchOperations.createIndex(Book.class);
        elasticsearchOperations.putMapping(Book.class);

        elasticsearchOperations.refresh(Book.class);


        Book saved = bookService.save(new Book(null, "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017"));
        bookService.save(new Book("1002", "Apache Lucene Basics", "Rambabu Posa", "13-MAR-2017"));
        bookService.save(new Book("1003", "Apache Solr Basics", "Rambabu Posa", "21-MAR-2017"));

        Page<Book> booksByAuthor = bookService.findByAuthor("Rambabu", PageRequest.of(0, 10));

        booksByAuthor.forEach(System.out::println);
    }
}
