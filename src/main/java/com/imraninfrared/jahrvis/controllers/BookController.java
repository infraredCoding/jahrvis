package com.imraninfrared.jahrvis.controllers;

import com.imraninfrared.jahrvis.models.books.Book;
import com.imraninfrared.jahrvis.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService _bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return new ResponseEntity<>(_bookService.getBooks(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return new ResponseEntity<>(_bookService.addBook(book), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        return _bookService.updateBook(book)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        Book book = new Book();
        book.setId(id);
        return _bookService.deleteBook(book)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
