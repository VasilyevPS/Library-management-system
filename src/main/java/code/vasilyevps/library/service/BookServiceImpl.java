package code.vasilyevps.library.service;

import code.vasilyevps.library.entity.book.Book;
import code.vasilyevps.library.entity.book.dto.BookCreateDto;
import code.vasilyevps.library.entity.book.dto.BookDto;
import code.vasilyevps.library.entity.book.dto.BookUpdateDto;
import code.vasilyevps.library.exception.ResourceNotFoundException;
import code.vasilyevps.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> getBook(long id) {
        return bookRepository.findById(id)
                .map(this::convertEntityToDto);
    }

    @Override
    @Transactional
    public BookDto createBook(BookCreateDto dto) {
        if (bookRepository.existsByIsbn(dto.isbn())) {
            throw new IllegalArgumentException("Книга с таким ISBN уже зарегистрирована");
        }
        Book newBook = convertCreateDtoToEntity(dto);
        Book savedBook = bookRepository.save(newBook);
        return convertEntityToDto(savedBook);
    }

    @Override
    @Transactional
    public void updateBook(long id, BookUpdateDto dto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга с ID " + id + " не найдена"));

        if (!existingBook.getIsbn().equals(dto.isbn()) && bookRepository.existsByIsbn(dto.isbn())) {
            throw new IllegalArgumentException("Книга с таким ISBN уже зарегистрирована");
        }

        updateEntity(existingBook, dto);
    }

    @Override
    @Transactional
    public void deleteBook(long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Книга с ID " + id + " не найдена");
        }
        bookRepository.deleteById(id);
    }

    private BookDto convertEntityToDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }

    private Book convertCreateDtoToEntity(BookCreateDto dto) {
        return new Book(
                null,
                dto.title(),
                dto.author(),
                dto.isbn(),
                dto.publicationYear(),
                dto.totalCopies(),
                dto.availableCopies()
        );
    }

    private void updateEntity(Book book, BookUpdateDto dto) {
        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setIsbn(dto.isbn());
        book.setPublicationYear(dto.publicationYear());
        book.setTotalCopies(dto.totalCopies());
        book.setAvailableCopies(dto.availableCopies());
    }
}
