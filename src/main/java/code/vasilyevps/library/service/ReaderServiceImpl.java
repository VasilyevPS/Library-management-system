package code.vasilyevps.library.service;

import code.vasilyevps.library.entity.reader.Reader;
import code.vasilyevps.library.entity.reader.dto.ReaderCreateDto;
import code.vasilyevps.library.entity.reader.dto.ReaderDto;
import code.vasilyevps.library.entity.reader.dto.ReaderUpdateDto;
import code.vasilyevps.library.exception.ResourceNotFoundException;
import code.vasilyevps.library.mapper.ReaderMapper;
import code.vasilyevps.library.repository.ReaderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final ReaderMapper readerMapper;

    public ReaderServiceImpl(ReaderRepository readerRepository, ReaderMapper readerMapper) {
        this.readerRepository = readerRepository;
        this.readerMapper = readerMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReaderDto> getAllReaders() {
        List<Reader> readers = readerRepository.findAll();
        return readers.stream()
                .map(readerMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReaderDto> getReader(long id) {
        return readerRepository.findById(id)
                .map(readerMapper::toDto);
    }

    @Override
    @Transactional
    public ReaderDto createReader(ReaderCreateDto dto) {
        if (readerRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Пользователь с таким email уже зарегистрирован");
        }
        Reader newReader = readerMapper.toEntity(dto);
        Reader savedReader = readerRepository.save(newReader);
        return readerMapper.toDto(savedReader);
    }

    @Override
    @Transactional
    public void updateReader(long id, ReaderUpdateDto dto) {
        Reader existingReader = readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + " не найден"));

        if (!existingReader.getEmail().equals(dto.email()) && readerRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Пользователь с таким email уже зарегистрирован");
        }

        readerMapper.updateEntityFromDto(dto, existingReader);
    }

    @Override
    @Transactional
    public void deleteReader(long id) {
        if (!readerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Пользователь с ID " + id + " не найден");
        }
        readerRepository.deleteById(id);
    }
}
