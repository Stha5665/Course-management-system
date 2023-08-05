package com.example.Group_project.entities;
// import
import javax.persistence.*;
import java.util.List;

@Entity// entity for table name
@Table(name = "MODULES")// table name
public class Module {
    @Id//id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int mId;// id
    private String moduleName;// name
    private String credits;//-
    private String mCode;//-- module code
    private String mDuration;//-- duration
    @Column(length = 100)
    private String description;//-- description
// many to one with course
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Course courses;//..
// one to many with staff//--
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "modules")
    private List<Staff> staff;
// constructor
    public Module() {
    }
//  constructor
    public Module(int mId, String moduleName, String credits, String mCode, String mDuration, String description, Course courses) {
        this.mId = mId;
        this.moduleName = moduleName;
        this.credits = credits;
        this.mCode = mCode;
        this.mDuration = mDuration;
        this.description = description;
        this.courses = courses;
    }
// getter and setter
    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Course getCourses() {
        return courses;
    }

    public void setCourses(Course courses) {
        this.courses = courses;
    }
}