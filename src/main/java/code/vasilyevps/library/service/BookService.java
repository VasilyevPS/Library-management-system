package code.vasilyevps.library.service;

import code.vasilyevps.library.entity.book.dto.BookCreateDto;
import code.vasilyevps.library.entity.book.dto.BookDto;
import code.vasilyevps.library.entity.book.dto.BookUpdateDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<BookDto> getAllBooks();

    Optional<BookDto> getBook(long id);

    BookDto createBook(@Valid BookCreateDto bookCreateDto);

    void updateBook(long id, @Valid BookUpdateDto bookUpdateDto);

    void deleteBook(long id);
}
