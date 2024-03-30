package com.example.demo.model;

import com.example.demo.model.enums.Role;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UserResponse {
	private long userId;
	private String name;
	private String email;
	private List<Role> roles;
	private PatientResponse patient;
}
