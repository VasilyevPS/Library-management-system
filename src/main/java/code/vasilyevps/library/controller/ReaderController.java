package code.vasilyevps.library.controller;

import code.vasilyevps.library.entity.reader.dto.ReaderCreateDto;
import code.vasilyevps.library.entity.reader.dto.ReaderDto;
import code.vasilyevps.library.entity.reader.dto.ReaderUpdateDto;
import code.vasilyevps.library.exception.ResourceNotFoundException;
import code.vasilyevps.library.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/readers")
public class ReaderController {

    private final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public List<ReaderDto> getAllReaders() {
        return readerService.getAllReaders();
    }

    @GetMapping("/{id}")
    public ReaderDto getReader(@PathVariable("id") long id) {
        return readerService.getReader(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    @PostMapping
    public ResponseEntity<ReaderDto> createReader(@Valid @RequestBody ReaderCreateDto readerCreateDto) {
        ReaderDto createdReader = readerService.createReader(readerCreateDto);
        return new ResponseEntity<>(createdReader, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReader(@PathVariable("id") long id,
                                             @Valid @RequestBody ReaderUpdateDto readerUpdateDto) {
        readerService.updateReader(id, readerUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable("id") long id) {
        readerService.deleteReader(id);
        return ResponseEntity.noContent().build();
    }
}
