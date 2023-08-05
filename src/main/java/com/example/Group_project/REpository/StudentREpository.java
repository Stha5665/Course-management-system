package com.example.Group_project.REpository;
//import
import com.example.Group_project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentREpository extends JpaRepository<Student, Integer> {
// for getting student by user name
    @Query("select s from Student s where s.email=:email") // query for getting user by username
    public Student getStudentByUserName(@Param("email") String email);
}
