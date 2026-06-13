package code.vasilyevps.library.utils;

import code.vasilyevps.library.entity.book.dto.BookCreateDto;
import code.vasilyevps.library.entity.reader.dto.ReaderCreateDto;
import code.vasilyevps.library.repository.BookRepository;
import code.vasilyevps.library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {
    public static final String ID = "/{id}";
    public static final String BOOK_CONTROLLER_URL = "/api/books";
    public static final String SEARCH = "/search";
    public static final String READER_CONTROLLER_URL = "/api/readers";

    private final BookCreateDto testBookDto = new BookCreateDto(
            "Чистый код",
            "Роберт Мартин",
            "978-5-4461-0950-0",
            2019,
            1,
            1
    );

    private final ReaderCreateDto testReaderDto = new ReaderCreateDto(
            "Джон",
            "Сноу",
            "snow@gmail.com",
            "+79991234567"
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    public void clearDB() {
        bookRepository.deleteAll();
        readerRepository.deleteAll();
    }

    public ResultActions addDefaultBook() throws Exception {
        return addBook(testBookDto);
    }

    public ResultActions addBook(BookCreateDto bookCreateDto) throws Exception {
        return perform(post(BOOK_CONTROLLER_URL)
                .content(objectMapper.writeValueAsString(bookCreateDto))
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions addDefaultReader() throws Exception {
        return addReader(testReaderDto);
    }

    public ResultActions addReader(ReaderCreateDto readerCreateDto) throws Exception {
        return perform(post(READER_CONTROLLER_URL)
                .content(objectMapper.writeValueAsString(readerCreateDto))
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

}
