package com.mahmoud.book.history;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mahmoud.book.book.Book;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer>  {

	@Query("""
			SELECT history
			FROM BookTransactioHistory history
			WHERE history.user.id=:userId
			""")
	Page<BookTransactionHistory> findAllBorrowedBooks(Integer userId, Pageable pageable);
	@Query("""
			SELECT history
			FROM BookTransactioHistory history
			WHERE history.book.owner.id=:userId
			""")
	Page<BookTransactionHistory> findAllReturnedBooks(Integer id, Pageable pageable);
	@Query("""
			SELECT
			(COUNT (*)>0) AS isBorrowed
			FROM BookTransactioHistory bookTransactioHistory
			WHERE bookTransactioHistory.user.id=:userId
			AND bookTransactioHistory.book.id=:bookId
			AND bookTransactioHistory.returnApproved=false
			""")
	boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);


}
