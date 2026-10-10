package com.natureevent.dto;

public class UserResponseDTO {

	private Long id;
	private String username;
	private String name;
	private String email;
	private String city;
	private String bio;

	public UserResponseDTO() {
	}

	public UserResponseDTO(Long id, String username, String name, String email, String city, String bio) {

		this.id = id;
		this.username = username;
		this.name = name;
		this.email = email;
		this.city = city;
		this.bio = bio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}
}