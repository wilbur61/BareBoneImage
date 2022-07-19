package com.example.BareBoneImage.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.BareBoneImage.model.Student;
import com.example.BareBoneImage.service.StudentService;


@Controller
public class StudentController {
	
	private StudentService studentService;

	public StudentController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}
	
//	@Autowired
//	private StudentService studentService;
	
	@Value("${uploadDir}")
	private String uploadFolder;

	// handler method to handle list students and return mode and view
	@GetMapping("/students")
	public String listStudents(Model model) {
		System.out.println("IN  StudentController->listStudents()");
		model.addAttribute("students", studentService.getAllStudents());
		return "students";
	}
	
	@GetMapping("/students/new")
	public String createStudentForm(Model model) {
		System.out.println("IN  StudentController->createStudentForm()");
		// javascript will handle the input!
		return "create_student";
		
	}
	
//===============================
	@PostMapping("/students/saveImageDetails")
	public @ResponseBody ResponseEntity<?> createStudent(
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, 
			@RequestParam("email") String email, 
			Model model, HttpServletRequest request,
			final @RequestParam("image") MultipartFile file) {
			try {
				//String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
				System.out.println(">>> uploadFolder="+uploadFolder);
				String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
				System.out.println("uploadDirectory="+uploadDirectory);
				String fileName = file.getOriginalFilename();
				String filePath = Paths.get(uploadDirectory, fileName).toString();
				System.out.println("FileName: " + file.getOriginalFilename());
				if (fileName == null || fileName.contains("..")) {
					model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
					return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
				}
				String[] fname = firstName.split(",");
				String[] lname = lastName.split(",");
				String[] em    = email.split(",");
				System.out.println("firstName="+fname[0]);
				System.out.println("lastName ="+lname[0]);
				System.out.println("email    ="+em[0]);

				try {
					File dir = new File(uploadDirectory);
					if (!dir.exists()) {
						System.out.println("Dir Created");
						dir.mkdirs();
					}
					// Save the file locally
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
					stream.write(file.getBytes());
					stream.close();
				} catch (Exception e) {
					System.out.println("!!!ERROR in create dir!!!");
					e.printStackTrace();
				}
				byte[] imageData = file.getBytes();
				Student stu = new Student();
				stu.setFirstName(fname[0]);
				stu.setLastName(lname[0]);
				stu.setEmail(em[0]);
				stu.setImage(imageData);
				studentService.saveStudent(stu);
				System.out.println("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
				return new ResponseEntity<>("Person Saved With File - " + fileName, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception!! on HTTP load="+e);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
	}
	//===============================
	@GetMapping("/image/display2/{id}")
	@ResponseBody
	void showStudentImage(@PathVariable("id") Long id, HttpServletResponse response, 
				Optional<Student> Student)
				throws ServletException, IOException {
			System.out.println("IN /image/display FOR ID="+id);
			Student = Optional.ofNullable(studentService.getStudentById(id));
			//System.out.println("IN /image/display DUMP2");
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(Student.get().getImage());
			response.getOutputStream().close();
			//System.out.println("IN /image/display DUMP3");
	}	
	
	// http://localhost:8080/image/studentDetails?id=4
	@GetMapping("/image/studentDetails")
	String showStudentDetails(@RequestParam("id") Long id, 
				              Student student, 
				              Model model) {
			try {
				//log.info("Id :: " + id);
				System.out.println("><><><><><>ENTRY POINT /image/studentDetails id="+id);
				if (id != 0) {
					//imageGallery = imageGalleryService.getImageById(id);
					student = studentService.getStudentById(id);
				
					//log.info("products :: " + imageGallery);
					System.out.println("><><><><><>IN /image/studentDetails FOR ID="+id);
				
					model.addAttribute("id", student.getId());
					model.addAttribute("firstName", student.getFirstName());
					model.addAttribute("lastName", student.getLastName());
					model.addAttribute("email", student.getEmail());
					return "studentdetails";
					
				}
			return "redirect:/home";
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/home";
			}	
	}	
	//===============================	
	
	
	@PostMapping("/students")
	public String saveStudent(@ModelAttribute("student") Student student) {
		System.out.println("IN  StudentController->saveStudent()");
		studentService.saveStudent(student);
		return "redirect:/students";
	}
	
	@GetMapping("/students/edit/{id}")
	public String editStudentForm(@PathVariable Long id, Model model) {
		System.out.println("IN  StudentController->editStudentForm()");
		model.addAttribute("student", studentService.getStudentById(id));
		return "edit_student";
	}

	@PostMapping("/students/{id}")
	public String updateStudent(@PathVariable Long id,
			@ModelAttribute("student") Student student,
			Model model) {
		
		// get student from database by id
		Student existingStudent = studentService.getStudentById(id);
		existingStudent.setId(id);
		existingStudent.setFirstName(student.getFirstName());
		existingStudent.setLastName(student.getLastName());
		existingStudent.setEmail(student.getEmail());
		
		// save updated student object
		studentService.updateStudent(existingStudent);
		return "redirect:/students";		
	}
	
	// handler method to handle delete student request
	
	@GetMapping("/students/{id}")
	public String deleteStudent(@PathVariable Long id) {
		System.out.println("IN  StudentController->deleteStudent()");
		studentService.deleteStudentById(id);
		return "redirect:/students";
	}
	
}