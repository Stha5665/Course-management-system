package com.example.Group_project.entities;

import javax.persistence.*;// import
@Entity
@Table(name = "ASSIGNMENTS")// entity for table
public class Assignment {
    @Id// generate id with auto increment
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int aId;//.. id
    private String title; // title

    private String startDate;// start date

    private String endDate;//..

    private String category;//..
    private String description;//--

    @ManyToOne// many to one relationship with module
    private Module module;
//...
    public Assignment() {
    }

    public Assignment(int aId, String title, String startDate, String endDate, String category, String description, Module module) {
        this.aId = aId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.description = description;
        this.module = module;
    }

    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
