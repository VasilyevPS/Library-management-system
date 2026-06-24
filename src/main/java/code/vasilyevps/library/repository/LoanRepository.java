package code.vasilyevps.library.repository;

import code.vasilyevps.library.entity.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByReaderId(Long readerId);

    @Query(value = "SELECT l FROM Loan l WHERE l.status = 'OVERDUE'")
    List<Loan> findOverdueLoans();

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Loan l SET l.status = 'OVERDUE' WHERE l.status = 'ISSUED' AND l.dueDate < :today")
    int updateStatusesToOverdue(@Param("today") LocalDate today);
}
