package code.vasilyevps.library.controller;

import code.vasilyevps.library.entity.loan.Loan;
import code.vasilyevps.library.entity.loan.LoanStatus;
import code.vasilyevps.library.entity.loan.dto.LoanCreateDto;
import code.vasilyevps.library.repository.BookRepository;
import code.vasilyevps.library.repository.LoanRepository;
import code.vasilyevps.library.repository.ReaderRepository;
import code.vasilyevps.library.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static code.vasilyevps.library.utils.TestUtils.LOAN_CONTROLLER_URL;
import static code.vasilyevps.library.utils.TestUtils.LOAN_READER;
import static code.vasilyevps.library.utils.TestUtils.LOAN_RETURN;
import static code.vasilyevps.library.utils.TestUtils.OVERDUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private TestUtils testUtils;

    @AfterEach
    void clearDB() {
        testUtils.clearDB();
    }

    @BeforeEach
    void addEntities() throws Exception {
        testUtils.addDefaultBook();
        testUtils.addDefaultReader();
    }

    @Test
    void testLoanBook() throws Exception {
        assertEquals(0, loanRepository.count());

        final Long bookId = bookRepository.findAll().getFirst().getId();
        final Long readerId = readerRepository.findAll().getFirst().getId();
        final LoanCreateDto dto = new LoanCreateDto(bookId, readerId, 1);

        testUtils.addLoan(dto)
                .andExpect(status().isCreated());
        assertEquals(1, loanRepository.count());
    }

    @Test
    void testLoanBookFail() throws Exception {
        assertEquals(0, loanRepository.count());

        final Long bookId = bookRepository.findAll().getFirst().getId();
        final Long readerId = readerRepository.findAll().getFirst().getId();
        final LoanCreateDto dtoBookNotExist = new LoanCreateDto(bookId - 1, readerId, 1);

        testUtils.addLoan(dtoBookNotExist)
                .andExpect(status().isNotFound());
        assertEquals(0, loanRepository.count());
    }

    @Test
    void testLoanBookNotAvailable() throws Exception {
        assertEquals(0, loanRepository.count());

        final Long bookId = bookRepository.findAll().getFirst().getId();
        final Long readerId = readerRepository.findAll().getFirst().getId();
        final LoanCreateDto dto = new LoanCreateDto(bookId, readerId, 1);

        testUtils.addLoan(dto)
                .andExpect(status().isCreated());
        assertEquals(1, loanRepository.count());

        testUtils.addLoan(dto)
                .andExpect(status().isConflict());
        assertEquals(1, loanRepository.count());
    }

    @Test
    void testReturnBook() throws Exception {
        final Long bookId = bookRepository.findAll().getFirst().getId();
        final Long readerId = readerRepository.findAll().getFirst().getId();
        final LoanCreateDto dto = new LoanCreateDto(bookId, readerId, 1);

        testUtils.addLoan(dto);
        final Long loanId = loanRepository.findAll().getFirst().getId();

        final var updateRequest = put(LOAN_CONTROLLER_URL + LOAN_RETURN, loanId);

        testUtils.perform(updateRequest).andExpect(status().isNoContent());
        Loan returnedLoan = loanRepository.findAll().getFirst();
        assertEquals("RETURNED", returnedLoan.getStatus().toString());
    }

    @Test
    void testReturnBookTwiceFail() throws Exception {
        final Long bookId = bookRepository.findAll().getFirst().getId();
        final Long readerId = readerRepository.findAll().getFirst().getId();
        final LoanCreateDto dto = new LoanCreateDto(bookId, readerId, 1);

        testUtils.addLoan(dto);
        final Long loanId = loanRepository.findAll().getFirst().getId();

        final var updateRequest = put(LOAN_CONTROLLER_URL + LOAN_RETURN, loanId);
        testUtils.perform(updateRequest).andExpect(status().isNoContent());
        testUtils.perform(updateRequest).andExpect(status().isBadRequest());
    }

    @Test
    void testReturnBookFail() throws Exception {
        final var updateRequest = put(LOAN_CONTROLLER_URL + LOAN_RETURN, 1L);
        testUtils.perform(updateRequest).andExpect(status().isNotFound());
    }

    @Test
    void testGetHistoryByReader() throws Exception {
        final Long bookId = bookRepository.findAll().getFirst().getId();
        final Long readerId = readerRepository.findAll().getFirst().getId();
        final LoanCreateDto dto = new LoanCreateDto(bookId, readerId, 1);

        testUtils.addLoan(dto);

        final var response = testUtils.perform(get(LOAN_CONTROLLER_URL + LOAN_READER, readerId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Loan> loans = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(loans).hasSize(1);
    }

    @Test
    void testGetHistoryByReaderFail() throws Exception {
        final Long bookId = bookRepository.findAll().getFirst().getId();
        final Long readerId = readerRepository.findAll().getFirst().getId();
        final LoanCreateDto dto = new LoanCreateDto(bookId, readerId, 1);

        testUtils.addLoan(dto);

        testUtils.perform(get(LOAN_CONTROLLER_URL + LOAN_READER, readerId - 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOverdueLoans() throws Exception {
        final Long bookId = bookRepository.findAll().getFirst().getId();
        final Long readerId = readerRepository.findAll().getFirst().getId();
        final LoanCreateDto dto = new LoanCreateDto(bookId, readerId, 1);

        testUtils.addLoan(dto);

        var response = testUtils.perform(get(LOAN_CONTROLLER_URL + OVERDUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<Loan> overdueLoans = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(overdueLoans).hasSize(0);

        final Loan loan = loanRepository.findAll().getFirst();
        loan.setStatus(LoanStatus.OVERDUE);
        loanRepository.save(loan);

        response = testUtils.perform(get(LOAN_CONTROLLER_URL + OVERDUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        overdueLoans = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(overdueLoans).hasSize(1);
    }

}
