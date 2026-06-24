package code.vasilyevps.library.entity.loan.dto;

import code.vasilyevps.library.entity.loan.LoanStatus;

import java.time.LocalDate;

public record LoanReaderDto(
        Long id,
        Long bookId,
        String bookTitle,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        LoanStatus status
) {
}
