package com.example.Group_project.REpository;

//import
import com.example.Group_project.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface StaffREpository extends JpaRepository<Staff, Integer> {
// for getting staff
    @Query("select s from Staff s where s.email=:email") // query for getting staff by username
    public Staff getStaffByUserName(@Param("email") String email);


}
