package com.example.demo.service;

import com.example.demo.entity.Patient;
import com.example.demo.entity.User;
import com.example.demo.model.CreateUserRequest;
import com.example.demo.model.PatientResponse;
import com.example.demo.model.UserResponse;
import com.example.demo.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void registerUser(CreateUserRequest createUserRequest) {
		User user = new User();
		user.setName(createUserRequest.getName());
		user.setEmail(createUserRequest.getEmail());
		user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
		user.setRolesList(createUserRequest.getRoles());

		Patient patient = new Patient();
		patient.setName(createUserRequest.getPatient().getName());
		patient.setGender(createUserRequest.getPatient().getGender());
		patient.setBloodGroup(createUserRequest.getPatient().getBloodGroup());
		patient.setAddress(createUserRequest.getPatient().getAddress());
		patient.setAge(createUserRequest.getPatient().getAge());
		patient.setContactNumber(createUserRequest.getPatient().getContactNumber());

		user.setPatient(patient);

		userRepository.save(user);
	}

	public UserResponse getUserByName(String name) {
		Optional<User> userOptional = userRepository.findByName(name);
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			UserResponse userResponse = new UserResponse();
			userResponse.setUserId(user.getUserId());
			userResponse.setName(user.getName());
			userResponse.setRoles(user.getRolesList());
			userResponse.setEmail(user.getEmail());

			PatientResponse patientResponse = new PatientResponse();
			patientResponse.setAddress(user.getPatient().getAddress());
			patientResponse.setAge(user.getPatient().getAge());
			patientResponse.setBloodGroup(user.getPatient().getBloodGroup());
			patientResponse.setContactNumber(user.getPatient().getContactNumber());
			patientResponse.setGender(user.getPatient().getGender());
			patientResponse.setName(user.getPatient().getName());
			patientResponse.setPatientId(user.getPatient().getPatientId());

			userResponse.setPatient(patientResponse);
			return userResponse;
		} else {
			// Handle case when user is not found
			return null; // Or return a default UserResponse with appropriate fields
		}
	}
	
	public Patient getPatientByUserName(String name) {
		return userRepository.findPatientIdByUserName(name);
	}

}
