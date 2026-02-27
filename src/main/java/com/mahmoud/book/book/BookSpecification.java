package com.mahmoud.book.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

	public static Specification<Book> withOwnerId(Integer ownerId){
		return (root ,query,cretriaBuilder)->cretriaBuilder.equal(root.get("owner").get("id"),ownerId);
	}

	
}
