package com.example.BareBoneImage.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BareBoneImage.model.Student;


public interface StudentRepository extends JpaRepository<Student, Long>{

}
