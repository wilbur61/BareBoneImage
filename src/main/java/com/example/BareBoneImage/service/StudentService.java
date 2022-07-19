package com.example.BareBoneImage.service;

import java.util.List;

import com.example.BareBoneImage.model.Student;


public interface StudentService {
	List<Student> getAllStudents();
	
	Student saveStudent(Student student);
	
	Student getStudentById(Long id);
	
	Student updateStudent(Student student);
	
	void deleteStudentById(Long id);
}
