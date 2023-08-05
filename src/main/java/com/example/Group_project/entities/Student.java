package com.example.Group_project.entities;

import javax.persistence.*;

@Entity
// for table student
public class Student {
    @Id// id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int sId;//id
    private String studentName;// name
    private String address;//--
    private String dateOfBirth;//;;
    private String email;// email
    // phone
    private String phone;//
//status
    private String status;
// many to one relationship with courses
    @ManyToOne
    private Course courses;
//..
    public Student() {
    }
//...
    public Student(int sId, String studentName, String address, String dateOfBirth, String email, String phone, Course course) {
        this.sId = sId;
        this.studentName = studentName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.courses = course;
    }
// getter and setters
    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Course getCourse() {
        return courses;
    }

    public void setCourse(Course course) {
        this.courses = course;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
