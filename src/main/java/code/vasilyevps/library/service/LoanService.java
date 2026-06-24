package code.vasilyevps.library.service;

import code.vasilyevps.library.entity.loan.dto.LoanCreateDto;
import code.vasilyevps.library.entity.loan.dto.LoanDto;
import code.vasilyevps.library.entity.loan.dto.LoanReaderDto;

import java.util.List;

public interface LoanService {

    LoanDto loanBook(LoanCreateDto loanCreateDto);

    void returnBook(long id);

    List<LoanReaderDto> getHistoryByReader(long readerId);

    List<LoanDto> getOverdueLoans();
}
