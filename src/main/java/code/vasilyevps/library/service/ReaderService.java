package code.vasilyevps.library.service;

import code.vasilyevps.library.entity.reader.dto.ReaderCreateDto;
import code.vasilyevps.library.entity.reader.dto.ReaderDto;
import code.vasilyevps.library.entity.reader.dto.ReaderUpdateDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface ReaderService {

    List<ReaderDto> getAllReaders();

    Optional<ReaderDto> getReader(long id);

    ReaderDto createReader(@Valid ReaderCreateDto readerCreateDto);

    void updateReader(long id, @Valid ReaderUpdateDto readerUpdateDto);

    void deleteReader(long id);
}
