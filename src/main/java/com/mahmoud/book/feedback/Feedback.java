package com.mahmoud.book.feedback;


import com.mahmoud.book.book.Book;
import com.mahmoud.book.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Entity
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor

public class Feedback extends BaseEntity{

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
	private Double note;
	private String comment;
}
