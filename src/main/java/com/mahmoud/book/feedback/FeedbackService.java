package com.mahmoud.book.feedback;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.mahmoud.book.book.Book;
import com.mahmoud.book.book.BookRepository;
import com.mahmoud.book.common.PageResponse;
import com.mahmoud.book.exception.OperationNotPermittedException;
import com.mahmoud.book.user.User;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {
	private final BookRepository bookRepository;
	private final FeedbackRepository feedbackRepository;
	private final FeedbackMapper feedbackMapper;
	public Integer save(FeedbackRequest request,Authentication connectedUser) {
		Book book=bookRepository.findById(request.bookId()).orElseThrow(()->new EntityNotFoundException("no book found with this id"));
		if (book.isArchived()||!book.isShareable()) {
			throw new OperationNotPermittedException("You Can't give a feedbacks");
		}
		User user=((User) connectedUser.getPrincipal());
		if (Objects.equals(book.getOwner().getId(),user.getId())) {
			throw new OperationNotPermittedException("You Can't give a feedbacks");
		}
		Feedback feedback=feedbackMapper.toFeedback(request);
		return feedbackRepository.save(feedback).getId();
	}
	public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser) {
		// TODO Auto-generated method stub
		Pageable pageaple=PageRequest.of(page, size);
		User user=((User) connectedUser.getPrincipal());
		Page<Feedback> feedbacks=feedbackRepository.findAllFeedbacksByBookId(bookId,pageaple);
		List<FeedbackResponse> feedbackResponse=feedbacks.stream()
				.map(f-> feedbackMapper.toFeedbackResponse(f,user.getId()))
				.toList();
		return new PageResponse<>(
				feedbackResponse,
				feedbacks.getNumber(),
				feedbacks.getSize(),
				feedbacks.getTotalElements(),
				feedbacks.getTotalPages(),
				feedbacks.isFirst(),
				feedbacks.isLast()
				);
	}
}
