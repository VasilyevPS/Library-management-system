package code.vasilyevps.library.controller;

import code.vasilyevps.library.entity.loan.dto.LoanCreateDto;
import code.vasilyevps.library.entity.loan.dto.LoanDto;
import code.vasilyevps.library.entity.loan.dto.LoanReaderDto;
import code.vasilyevps.library.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanDto> loanBook(@Valid @RequestBody LoanCreateDto loanCreateDto) {
        LoanDto createdLoan = loanService.loanBook(loanCreateDto);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @PutMapping("{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable("id") long id) {
        loanService.returnBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("reader/{readerId}")
    public List<LoanReaderDto> getHistoryByReader(@PathVariable("readerId") long readerId) {
        return loanService.getHistoryByReader(readerId);
    }

    @GetMapping("overdue")
    public List<LoanDto> getOverdueLoans() {
        return loanService.getOverdueLoans();
    }
}
