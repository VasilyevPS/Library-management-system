package code.vasilyevps.library.entity.loan.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LoanCreateDto(
        @NotNull(message = "Требуется указать ID книги")
        Long bookId,

        @NotNull(message = "Требуется указать ID читателя")
        Long readerId,

        @NotNull(message = "Число дней не может быть пустым")
        @Min(value = 1, message = "Число дней не может быть меньше 1")
        Integer loanDays
) {
}
