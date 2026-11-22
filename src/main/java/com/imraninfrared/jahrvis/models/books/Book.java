package com.imraninfrared.jahrvis.models.books;

import com.imraninfrared.jahrvis.enums.books.ReadingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private int totalPages;
    private int pagesRead;
    private ReadingStatus isReading = ReadingStatus.BACKLOG;
}
