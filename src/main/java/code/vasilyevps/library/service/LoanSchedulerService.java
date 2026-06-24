package code.vasilyevps.library.service;

import code.vasilyevps.library.repository.LoanRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class LoanSchedulerService {

    private final LoanRepository loanRepository;

    public LoanSchedulerService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void runStatusUpdateToOverdue() {
        LocalDate today = LocalDate.now();
        loanRepository.updateStatusesToOverdue(today);
    }
}
