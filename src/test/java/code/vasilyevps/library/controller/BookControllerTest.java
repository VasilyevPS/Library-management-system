package code.vasilyevps.library.controller;

import code.vasilyevps.library.entity.book.Book;
import code.vasilyevps.library.entity.book.dto.BookCreateDto;
import code.vasilyevps.library.entity.book.dto.BookUpdateDto;
import code.vasilyevps.library.repository.BookRepository;
import code.vasilyevps.library.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static code.vasilyevps.library.utils.TestUtils.BOOK_CONTROLLER_URL;
import static code.vasilyevps.library.utils.TestUtils.ID;
import static code.vasilyevps.library.utils.TestUtils.SEARCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestUtils testUtils;

    @AfterEach
    void clearDB() {
        testUtils.clearDB();
    }

    @Test
    void testCreateBook() throws Exception {
        assertEquals(0, bookRepository.count());
        testUtils.addDefaultBook()
                .andExpect(status().isCreated());
        assertEquals(1, bookRepository.count());
    }

    @Test
    void testCreateBookTwiceFail() throws Exception {
        assertEquals(0, bookRepository.count());
        testUtils.addDefaultBook()
                .andExpect(status().isCreated());
        testUtils.addDefaultBook()
                .andExpect(status().isBadRequest());
        assertEquals(1, bookRepository.count());
    }

    @Test
    void testCreateBookFail() throws Exception {
        final BookCreateDto dto = new BookCreateDto(
                " ",
                "Роберт Мартин",
                "978-5-4461-0950-0",
                2019,
                1,
                1
        );
        assertEquals(0, bookRepository.count());
        testUtils.addBook(dto)
                .andExpect(status().isBadRequest());
        assertEquals(0, bookRepository.count());
    }

    @Test
    void testGetBookById() throws Exception {
        testUtils.addDefaultBook();
        final Book expectedBook = bookRepository.findAll().getFirst();

        final var response = testUtils.perform(
                        get(BOOK_CONTROLLER_URL + ID, expectedBook.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Book book = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedBook.getId(), book.getId());
        assertEquals(expectedBook.getTitle(), book.getTitle());
        assertEquals(expectedBook.getIsbn(), book.getIsbn());
    }

    @Test
    void testGetBookByIdNotExist() throws Exception {
        final int notExistedBookId = 1;

        testUtils.perform(get(BOOK_CONTROLLER_URL + ID, notExistedBookId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllBooks() throws Exception {
        testUtils.addDefaultBook();
        final var response = testUtils.perform(get(BOOK_CONTROLLER_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Book> books = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(books).hasSize(1);
    }

    @Test
    void testUpdateBook() throws Exception {
        testUtils.addDefaultBook();
        final Long bookId = bookRepository.findAll().getFirst().getId();

        final BookUpdateDto dto = new BookUpdateDto(
                "Чистый код",
                "Р. Мартин",
                "978-0-1353-9852-4",
                2019,
                100,
                100
        );

        final var updateRequest = put(BOOK_CONTROLLER_URL + ID, bookId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON);

        testUtils.perform(updateRequest).andExpect(status().isNoContent());

        assertTrue(bookRepository.existsByIsbn("978-0-1353-9852-4"));
        assertFalse(bookRepository.existsByIsbn("978-5-4461-0950-0"));
    }

    @Test
    void testUpdateBookNotExist() throws Exception {
        testUtils.addDefaultBook();
        final Long notExistingBookId = 2L;

        final BookUpdateDto dto = new BookUpdateDto(
                "Чистый код",
                "Р. Мартин",
                "978-0-1353-9852-4",
                2019,
                100,
                100
        );

        final var updateRequest = put(BOOK_CONTROLLER_URL + ID, notExistingBookId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON);

        testUtils.perform(updateRequest).andExpect(status().isNotFound());

        assertFalse(bookRepository.existsByIsbn("978-0-1353-9852-4"));
        assertTrue(bookRepository.existsByIsbn("978-5-4461-0950-0"));
    }

    @Test
    void testUpdateBookIncorrectData() throws Exception {
        testUtils.addDefaultBook();
        final Long bookId = bookRepository.findAll().getFirst().getId();

        final BookUpdateDto dto = new BookUpdateDto(
                "Чистый код",
                "Р. Мартин",
                "123-4-5678-901",
                2019,
                100,
                100
        );

        final var updateRequest = put(BOOK_CONTROLLER_URL + ID, bookId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON);

        testUtils.perform(updateRequest).andExpect(status().isBadRequest());

        assertFalse(bookRepository.existsByIsbn("123-4-5678-901"));
        assertTrue(bookRepository.existsByIsbn("978-5-4461-0950-0"));
    }

    @Test
    void testDeleteBook() throws Exception {
        testUtils.addDefaultBook();
        final Long bookId = bookRepository.findAll().getFirst().getId();

        testUtils.perform(delete(BOOK_CONTROLLER_URL + ID, bookId))
                .andExpect(status().isNoContent());

        assertEquals(0, bookRepository.count());
    }

    @Test
    void testDeleteBookNotExist() throws Exception {
        testUtils.addDefaultBook();
        final Long notExistingBookId = 2L;

        testUtils.perform(delete(BOOK_CONTROLLER_URL + ID, notExistingBookId))
                .andExpect(status().isNotFound());

        assertEquals(1, bookRepository.count());
    }

    @Test
    void testSearch() throws Exception {
        testUtils.addDefaultBook();

        BookCreateDto secondBook = new BookCreateDto(
                "Java. Эффективное программирование",
                "Джошуа Блох",
                "978-0-2013-1005-4",
                2019,
                1,
                1);
        testUtils.addBook(secondBook);

        assertEquals(2, bookRepository.count());

        final var response = testUtils.perform(get(BOOK_CONTROLLER_URL + SEARCH)
                        .queryParam("title", "Java"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Book> books = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(books).hasSize(1);
    }

    @Test
    void testSearchWithNoParameters() throws Exception {
        testUtils.addDefaultBook();

        BookCreateDto secondBook = new BookCreateDto(
                "Java. Эффективное программирование",
                "Джошуа Блох",
                "978-0-2013-1005-4",
                2019,
                1,
                1);
        testUtils.addBook(secondBook);

        assertEquals(2, bookRepository.count());

        final var response = testUtils.perform(get(BOOK_CONTROLLER_URL + SEARCH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Book> books = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(books).hasSize(2);
    }

}
