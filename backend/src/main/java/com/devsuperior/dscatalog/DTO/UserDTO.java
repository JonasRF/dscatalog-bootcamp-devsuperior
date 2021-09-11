package com.devsuperior.dscatalog.DTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.devsuperior.dscatalog.entities.User;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	
	@NotBlank(message = "Campo obrigatório")
	private String firstName;
	private String LastName;
	
	@Email(message = "Entrar com um email válido")
	private String email;
	private String password;
	
	Set<RoleDTO> roles = new HashSet<>();
	
	public UserDTO() {
	}

	public UserDTO(Long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.LastName = lastName;
		this.email = email;
		this.password = password;
	}
	
	public UserDTO(User entity) {
		id = entity.getId();
		firstName = entity.getFirstName();
		LastName = entity.getLastName();
		email = entity.getEmail();
		entity.getRoles().forEach(rule -> this.roles.add(new RoleDTO(rule)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

	
}
