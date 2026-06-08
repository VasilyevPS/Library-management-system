package code.vasilyevps.library.entity.book.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;

public record BookUpdateDto(
        @NotBlank(message = "Название книги не может быть пустым")
        String title,

        @NotBlank(message = "Автор не может быть пустым")
        String author,

        @NotBlank(message = "ISBN не должен быть пустым")
        @ISBN(type = ISBN.Type.ANY, message = "Введенный ISBN не существует. Проверьте правильность цифр")
        String isbn,

        @NotNull(message = "Год публикации обязателен")
        @Min(value = 1455, message = "Год публикации не может быть раньше появления книгопечатания")
        @Max(value = 2026, message = "Год публикации не может быть в будущем")
        Integer publicationYear,

        @NotNull(message = "Укажите общее количество копий")
        @Min(value = 0, message = "Количество копий не может быть отрицательным")
        Integer totalCopies,

        @NotNull(message = "Укажите доступное количество копий")
        @Min(value = 0, message = "Количество копий не может быть отрицательным")
        Integer availableCopies
) { }
