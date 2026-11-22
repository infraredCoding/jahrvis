package com.imraninfrared.jahrvis.services;

import com.imraninfrared.jahrvis.models.books.Book;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface BookService {
    List<Book> getBooks();
    Book addBook(Book book);
    Optional<Book> updateBook(Book book);
    boolean deleteBook(Book book);
}
