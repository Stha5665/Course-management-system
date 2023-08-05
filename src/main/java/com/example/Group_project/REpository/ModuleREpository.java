package com.example.Group_project.REpository;

import com.example.Group_project.entities.Course;
import com.example.Group_project.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleREpository extends JpaRepository<Module, Integer> {
// for list of module by course
    @Query("from Module as m where m.courses.cId =:courseId")
    public List<Module> findModuleByCourse(@Param("courseId") int courseId);

    // for module by id
    @Query("from Module as m where m.mId =:mId")
    public List<Module> findModuleById(@Param("mId") int mId);
}
