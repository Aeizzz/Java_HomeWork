package edu.wfu.bean;

import java.util.List;

public class Classy {
    private String id;
    private String name;
    private List<Student> studentList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Override
    public String toString() {
        return "Classy{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", studentList=" + studentList +
                '}';
    }
}
