package code.vasilyevps.library.service;

import code.vasilyevps.library.entity.book.Book;
import code.vasilyevps.library.entity.loan.Loan;
import code.vasilyevps.library.entity.loan.LoanStatus;
import code.vasilyevps.library.entity.loan.dto.LoanCreateDto;
import code.vasilyevps.library.entity.loan.dto.LoanDto;
import code.vasilyevps.library.entity.loan.dto.LoanReaderDto;
import code.vasilyevps.library.entity.reader.Reader;
import code.vasilyevps.library.exception.ResourceNotFoundException;
import code.vasilyevps.library.mapper.LoanMapper;
import code.vasilyevps.library.repository.BookRepository;
import code.vasilyevps.library.repository.LoanRepository;
import code.vasilyevps.library.repository.ReaderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final LoanMapper loanMapper;

    public LoanServiceImpl(LoanRepository loanRepository,
                           BookRepository bookRepository,
                           ReaderRepository readerRepository,
                           LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.loanMapper = loanMapper;
    }

    @Override
    @Transactional
    public LoanDto loanBook(LoanCreateDto loanCreateDto) {
        Long bookId = loanCreateDto.bookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Книга с ID " + bookId + " не найдена"));

        Integer availableCopies = book.getAvailableCopies();
        if (availableCopies == 0) {
            throw new IllegalStateException("Книги с ID " + bookId + " сейчас нет в наличии");
        }

        Long readerId = loanCreateDto.readerId();
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + readerId + " не найден"));

        Integer loanDays = loanCreateDto.loanDays();
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(loanDays);

        Loan loan = new Loan(
                book, reader,
                loanDate, dueDate,
                LoanStatus.ISSUED
        );
        loanRepository.save(loan);
        book.setAvailableCopies(availableCopies - 1);

        return loanMapper.toDto(loan);
    }

    @Override
    @Transactional
    public void returnBook(long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись с ID " + id + " не найдена"));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new IllegalArgumentException("Книга уже была возвращена ранее");
        }
        Book book = loan.getBook();
        Integer availableCopies = book.getAvailableCopies();

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);
        book.setAvailableCopies(availableCopies + 1);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanReaderDto> getHistoryByReader(long readerId) {
        if (!readerRepository.existsById(readerId)) {
            throw new ResourceNotFoundException("Пользователь с ID " + readerId + " не найден");
        }
        List<Loan> loans = loanRepository.findByReaderId(readerId);
        return loans.stream()
                .map(loanMapper::toReaderDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDto> getOverdueLoans() {
        List<Loan> overdueLoans = loanRepository.findOverdueLoans();
        return overdueLoans.stream()
                .map(loanMapper::toDto)
                .toList();
    }
}
