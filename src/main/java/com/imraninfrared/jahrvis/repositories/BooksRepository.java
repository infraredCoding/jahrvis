package com.imraninfrared.jahrvis.repositories;

import com.imraninfrared.jahrvis.models.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {
}
