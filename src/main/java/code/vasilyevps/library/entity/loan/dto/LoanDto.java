package code.vasilyevps.library.entity.loan.dto;

import java.time.LocalDate;

public record LoanDto(
        Long id,
        Long bookId,
        String bookTitle,
        Long readerId,
        String readerName,
        LocalDate loanDate,
        LocalDate dueDate
) {
}
