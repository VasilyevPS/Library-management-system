package code.vasilyevps.library.entity.book.dto;

public record BookDto(
        Long id,
        String title,
        String author,
        String isbn,
        Integer publicationYear,
        Integer totalCopies,
        Integer availableCopies
) { }
