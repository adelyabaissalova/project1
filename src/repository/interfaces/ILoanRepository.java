package repository.interfaces;

import models.dto.LoanInfo;

import java.util.List;

public interface ILoanRepository {
    boolean borrowBook(int bookId, int userId);
    boolean returnBookAsUser(int bookId, int userId);
    boolean returnBookAsStaff(int bookId);

    List<LoanInfo> getLoansByUser(int userId);
    LoanInfo getActiveLoanByBook(int bookId);
}
