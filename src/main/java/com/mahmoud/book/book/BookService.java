package com.mahmoud.book.book;

import java.beans.IntrospectionException;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mahmoud.book.common.PageResponse;
import com.mahmoud.book.exception.OperationNotPermittedException;
import com.mahmoud.book.history.BookTransactionHistory;
import com.mahmoud.book.history.BookTransactionHistoryRepository;
import com.mahmoud.book.user.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
	
	private final BookRepository bookRepos;
	private final BookMapper bookMapper;
	private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
	public Integer save( BookRequest request, Authentication connectedUser) {
		// TODO Auto-generated method stub
		User user=((User) connectedUser.getPrincipal());
		Book book=bookMapper.toBook(request);
		book.setOwner(user);
		return bookRepos.save(book).getId();
	}
	public  BookResponse findBookById(Integer bookId) {
		// TODO Auto-generated method stub
		return bookRepos.findById(bookId).map(bookMapper::toBookResponse)
				.orElseThrow(()->new EntityNotFoundException("No Book Found with Id :"+ bookId));
	}
	public  PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
		// TODO Auto-generated method stub
		User user=((User) connectedUser.getPrincipal());
		Pageable pageable=PageRequest.of(page, size,Sort.by("createdDate").descending());
		Page<Book> books=bookRepos.findAllDisplayableBooks(pageable,user.getId());
		List<BookResponse> bookResponse=books.stream().map(bookMapper::toBookResponse)
				.toList();
		return new PageResponse<>(
				bookResponse,
				books.getNumber(),
				books.getSize(),
				books.getTotalElements(),
				books.getTotalPages(),
				books.isFirst(),
				books.isLast()
				);
	}
	public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
		// TODO Auto-generated method stub
		User user=((User) connectedUser.getPrincipal());
		Pageable pageable=PageRequest.of(page, size,Sort.by("createdDate").descending());
		Page<Book> books=bookRepos.findAll(BookSpecification.withOwnerId(user.getId()),pageable);

		List<BookResponse> bookResponse=books.stream().map(bookMapper::toBookResponse)
				.toList();
		return new PageResponse<>(
				bookResponse,
				books.getNumber(),
				books.getSize(),
				books.getTotalElements(),
				books.getTotalPages(),
				books.isFirst(),
				books.isLast()
				);

	}
	public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
		// TODO Auto-generated method stub
		User user=((User) connectedUser.getPrincipal());
		Pageable pageable=PageRequest.of(page, size,Sort.by("createdDate").descending());
		Page<BookTransactionHistory> allBorrowedBooks=bookTransactionHistoryRepository.findAllBorrowedBooks(user.getId(),pageable);
		List<BorrowedBookResponse> bookResponse=allBorrowedBooks.stream()
				.map(bookMapper::toBorrowedBookResponse)
				.toList();		
		return new PageResponse<>(
				bookResponse,
				allBorrowedBooks.getNumber(),
				allBorrowedBooks.getSize(),
				allBorrowedBooks.getTotalElements(),
				allBorrowedBooks.getTotalPages(),
				allBorrowedBooks.isFirst(),
				allBorrowedBooks.isLast()
				);
	}
	public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
		User user=((User) connectedUser.getPrincipal());
		Pageable pageable=PageRequest.of(page, size,Sort.by("createdDate").descending());
		Page<BookTransactionHistory> allBorrowedBooks=bookTransactionHistoryRepository.findAllReturnedBooks(user.getId(),pageable);
		List<BorrowedBookResponse> bookResponse=allBorrowedBooks.stream()
				.map(bookMapper::toBorrowedBookResponse)
				.toList();		
		return new PageResponse<>(
				bookResponse,
				allBorrowedBooks.getNumber(),
				allBorrowedBooks.getSize(),
				allBorrowedBooks.getTotalElements(),
				allBorrowedBooks.getTotalPages(),
				allBorrowedBooks.isFirst(),
				allBorrowedBooks.isLast()
				);
	}
	public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
		// TODO Auto-generated method stub
		Book book=bookRepos.findById(bookId).orElseThrow(()->new EntityNotFoundException("No book Found withthis Id"));
		User user=((User) connectedUser.getPrincipal());
		if (!Objects.equals(book.getOwner().getBooks(),user.getId())) {
			throw new OperationNotPermittedException("You Can't Update Books Shareable status");
		}
		book.setShareable(!book.isShareable());
		bookRepos.save(book);
		return bookId;
	}
	public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
		// TODO Auto-generated method stub
		Book book=bookRepos.findById(bookId).orElseThrow(()->new EntityNotFoundException("No book Found withthis Id"));
		User user=((User) connectedUser.getPrincipal());
		if (!Objects.equals(book.getOwner().getId(),user.getId())) {
			throw new OperationNotPermittedException("You Can't Update Books Archived status");
		}
		book.setArchived(!book.isArchived());
		bookRepos.save(book);
		return bookId;	}
	public Integer borrowBook(Integer bookId, Authentication connectedUser) {
		// TODO Auto-generated method stub
		Book book=bookRepos.findById(bookId).orElseThrow(()->new EntityNotFoundException("No book Found withthis Id"));
		User user=((User) connectedUser.getPrincipal());
		if (book.isArchived()||!book.isShareable()) {
			throw new OperationNotPermittedException("You Can't Update Books Archived status");
		}
		if (Objects.equals(book.getOwner().getId(),user.getId())) {
			throw new OperationNotPermittedException("You Can't borrow Your own Book");
		}
		final boolean isAlreadyBorrowed=bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId,user.getId());
		
		return null;
	}
}
