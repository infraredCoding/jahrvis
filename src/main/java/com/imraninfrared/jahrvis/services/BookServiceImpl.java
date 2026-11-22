package com.imraninfrared.jahrvis.services;

import com.imraninfrared.jahrvis.models.books.Book;
import com.imraninfrared.jahrvis.repositories.BooksRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BooksRepository _booksRepository;

    @Override
    public List<Book> getBooks() {
        return _booksRepository.findAll();
    }

    @Override
    public Book addBook(Book book) {
        return _booksRepository.save(book);
    }

    @Override
    public Optional<Book> updateBook(Book book) {
        Optional<Book> bookOptional = _booksRepository.findById(book.getId());
        if (bookOptional.isPresent()) {
            _booksRepository.save(book);
            return bookOptional;
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteBook(Book book) {
        Optional<Book> bookOptional = _booksRepository.findById(book.getId());
        if (bookOptional.isPresent()) {
            _booksRepository.delete(book);
            return true;
        }
        return false;
    }
}
