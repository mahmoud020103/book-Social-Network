package com.mahmoud.book.feedback;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.mahmoud.book.book.Book;

@Service
public class FeedbackMapper {
	Feedback toFeedback(FeedbackRequest request) {
		return Feedback.builder()
				.note(request.note())
				.comment(request.comment())
				.book(Book.builder()
						.id(request.bookId())
						.archived(false)
						.shareable(false)
						.build()
						)
				.build();
	}

	public FeedbackResponse toFeedbackResponse(Feedback f, Integer id) {
		// TODO Auto-generated method stub
		return FeedbackResponse.builder()
				.note(f.getNote())
				.comment(f.getComment())
				.ownFeedback(Objects.equals(f.getCreatedBy(), id))
				.build()
				;
	}
}
