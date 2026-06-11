package code.vasilyevps.library.service;

import code.vasilyevps.library.entity.book.Book;
import code.vasilyevps.library.entity.book.BookSpecs;
import code.vasilyevps.library.entity.book.dto.BookCreateDto;
import code.vasilyevps.library.entity.book.dto.BookDto;
import code.vasilyevps.library.entity.book.dto.BookSearchFilter;
import code.vasilyevps.library.entity.book.dto.BookUpdateDto;
import code.vasilyevps.library.exception.ResourceNotFoundException;
import code.vasilyevps.library.mapper.BookMapper;
import code.vasilyevps.library.repository.BookRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> getBook(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public BookDto createBook(BookCreateDto dto) {
        if (bookRepository.existsByIsbn(dto.isbn())) {
            throw new IllegalArgumentException("Книга с таким ISBN уже зарегистрирована");
        }
        Book newBook = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(newBook);
        return bookMapper.toDto(savedBook);
    }

    @Override
    @Transactional
    public void updateBook(long id, BookUpdateDto dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга с ID " + id + " не найдена"));

        if (!existingBook.getIsbn().equals(dto.isbn()) && bookRepository.existsByIsbn(dto.isbn())) {
            throw new IllegalArgumentException("Книга с таким ISBN уже зарегистрирована");
        }

        bookMapper.updateEntityFromDto(dto, existingBook);
    }

    @Override
    @Transactional
    public void deleteBook(long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Книга с ID " + id + " не найдена");
        }
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> search(BookSearchFilter filter) {
        Specification<Book> spec = Specification.where(BookSpecs.hasTitle(filter.getTitle()))
                .and(BookSpecs.hasAuthor(filter.getAuthor()))
                .and(BookSpecs.hasIsbn(filter.getIsbn()))
                .and(BookSpecs.hasPublicationYear(filter.getPublicationYear()))
                .and(BookSpecs.hasTotalCopies(filter.getTotalCopies()))
                .and(BookSpecs.hasAvailableCopies(filter.getAvailableCopies()));

        List<Book> filteredBooks = bookRepository.findAll(spec);
        return filteredBooks.stream()
                .map(bookMapper::toDto)
                .toList();
    }

}
