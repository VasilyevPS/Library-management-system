package code.vasilyevps.library.controller;

import code.vasilyevps.library.entity.reader.Reader;
import code.vasilyevps.library.entity.reader.dto.ReaderCreateDto;
import code.vasilyevps.library.entity.reader.dto.ReaderUpdateDto;
import code.vasilyevps.library.repository.ReaderRepository;
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

import static code.vasilyevps.library.utils.TestUtils.ID;
import static code.vasilyevps.library.utils.TestUtils.READER_CONTROLLER_URL;
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
public class ReaderControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private TestUtils testUtils;

    @AfterEach
    void clearDB() {
        testUtils.clearDB();
    }

    @Test
    void testCreateReader() throws Exception {
        assertEquals(0, readerRepository.count());
        testUtils.addDefaultReader()
                .andExpect(status().isCreated());
        assertEquals(1, readerRepository.count());
    }

    @Test
    void testCreateReaderTwiceFail() throws Exception {
        assertEquals(0, readerRepository.count());
        testUtils.addDefaultReader()
                .andExpect(status().isCreated());
        testUtils.addDefaultReader()
                .andExpect(status().isBadRequest());
        assertEquals(1, readerRepository.count());
    }

    @Test
    void testCreateReaderFail() throws Exception {
        final ReaderCreateDto dto = new ReaderCreateDto(
                "Джон",
                "Сноу",
                "snow",
                "+79991234567"
        );
        assertEquals(0, readerRepository.count());
        testUtils.addReader(dto)
                .andExpect(status().isBadRequest());
        assertEquals(0, readerRepository.count());
    }

    @Test
    void testGetReaderById() throws Exception {
        testUtils.addDefaultReader();
        final Reader expectedReader = readerRepository.findAll().getFirst();

        final var response = testUtils.perform(
                        get(READER_CONTROLLER_URL + ID, expectedReader.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Reader reader = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedReader.getId(), reader.getId());
        assertEquals(expectedReader.getEmail(), reader.getEmail());
        assertEquals(expectedReader.getFirstName(), reader.getFirstName());
    }

    @Test
    void testGetReaderByIdNotExist() throws Exception {
        final int notExistedReaderId = 1;

        testUtils.perform(get(READER_CONTROLLER_URL + ID, notExistedReaderId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllReaders() throws Exception {
        testUtils.addDefaultReader();
        final var response = testUtils.perform(get(READER_CONTROLLER_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Reader> readers = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(readers).hasSize(1);
    }

    @Test
    void testUpdateReader() throws Exception {
        testUtils.addDefaultReader();
        final Long readerId = readerRepository.findAll().getFirst().getId();

        final ReaderUpdateDto dto = new ReaderUpdateDto(
                "Джон",
                "Сноу",
                "kinginthenorth@gmail.com",
                "+79991234567"
        );

        final var updateRequest = put(READER_CONTROLLER_URL + ID, readerId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON);

        testUtils.perform(updateRequest).andExpect(status().isNoContent());

        assertTrue(readerRepository.existsByEmail("kinginthenorth@gmail.com"));
        assertFalse(readerRepository.existsByEmail("snow@gmail.com"));
    }

    @Test
    void testUpdateReaderNotExist() throws Exception {
        testUtils.addDefaultReader();
        final Long notExistingReaderId = 0L;

        final ReaderUpdateDto dto = new ReaderUpdateDto(
                "Джон",
                "Сноу",
                "kinginthenorth@gmail.com",
                "+79991234567"
        );

        final var updateRequest = put(READER_CONTROLLER_URL + ID, notExistingReaderId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON);

        testUtils.perform(updateRequest).andExpect(status().isNotFound());

        assertFalse(readerRepository.existsByEmail("kinginthenorth@gmail.com"));
        assertTrue(readerRepository.existsByEmail("snow@gmail.com"));
    }

    @Test
    void testUpdateReaderIncorrectData() throws Exception {
        testUtils.addDefaultReader();
        final Long readerId = readerRepository.findAll().getFirst().getId();

        final ReaderUpdateDto dto = new ReaderUpdateDto(
                "Джон",
                "Сноу",
                "snow",
                "+79991234567"
        );

        final var updateRequest = put(READER_CONTROLLER_URL + ID, readerId)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON);

        testUtils.perform(updateRequest).andExpect(status().isBadRequest());

        assertFalse(readerRepository.existsByEmail("snow"));
        assertTrue(readerRepository.existsByEmail("snow@gmail.com"));
    }

    @Test
    void testDeleteReader() throws Exception {
        testUtils.addDefaultReader();
        final Long readerId = readerRepository.findAll().getFirst().getId();

        testUtils.perform(delete(READER_CONTROLLER_URL + ID, readerId))
                .andExpect(status().isNoContent());

        assertEquals(0, readerRepository.count());
    }

    @Test
    void testDeleteReaderNotExist() throws Exception {
        testUtils.addDefaultReader();
        final Long notExistingReaderId = 2L;

        testUtils.perform(delete(READER_CONTROLLER_URL + ID, notExistingReaderId))
                .andExpect(status().isNotFound());

        assertEquals(1, readerRepository.count());
    }
}
