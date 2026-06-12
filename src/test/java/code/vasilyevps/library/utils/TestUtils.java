package code.vasilyevps.library.utils;

import code.vasilyevps.library.entity.book.dto.BookCreateDto;
import code.vasilyevps.library.repository.BookRepository;
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

    private final BookCreateDto testBookDto = new BookCreateDto(
            "Чистый код",
            "Роберт Мартин",
            "978-5-4461-0950-0",
            2019,
            1,
            1
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    public void clearDB() {
        bookRepository.deleteAll();
    }

    public ResultActions addDefaultBook() throws Exception {
        return addBook(testBookDto);
    }

    public ResultActions addBook(BookCreateDto bookCreateDto) throws Exception {
        return perform(post(BOOK_CONTROLLER_URL)
                .content(objectMapper.writeValueAsString(bookCreateDto))
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

}
