package com.mahmoud.book.user;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mahmoud.book.book.Book;
import com.mahmoud.book.history.BookTransactionHistory;
import com.mahmoud.book.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails,Principal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String firstName;
	private String lastName;
	private LocalDate birthOfDate;
	@Column(unique = true)
	private String email;
	private String password;
	private boolean acountLocked;
	private boolean enabled;
	@CreatedDate
	@Column(nullable = false,updatable = false)
	private LocalDateTime createdDate;
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	@ManyToMany(fetch = FetchType.EAGER)
	private List <Role> roles;
	@OneToMany(mappedBy = "owner")
	private List<Book> books;
	@OneToMany(mappedBy = "user")
	private List<BookTransactionHistory> histories;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return roles
				.stream()
				.map(r->new SimpleGrantedAuthority(r.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public @Nullable String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return email;
	}
	public String getFullName() {
		return firstName+" "+lastName;
	}

}
