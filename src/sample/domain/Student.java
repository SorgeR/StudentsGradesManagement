package sample.domain;

import sample.domain.interfaces.HasID;

import java.util.Objects;

public class Student implements HasID<Integer> {


    private Integer id;
    private String name;
    private Integer group;
    private Integer subgroup;
    private String email;
    private String teachersName;
    private Boolean deleted;

    public Student(Integer id, String name, Integer group, Integer subgroup, String email, String teachersName, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.subgroup = subgroup;
        this.email = email;
        this.teachersName = teachersName;
        this.deleted = deleted;
    }

    public Student(String name, Integer group, Integer subgroup, String email, String teachersName) {
        this.name = name;
        this.group = group;
        this.subgroup = subgroup;
        this.email = email;
        this.teachersName = teachersName;
    }

    public Student(Integer id, String name, Integer group, Integer subgroup, String email, String teachersName) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.subgroup = subgroup;
        this.email = email;
        this.teachersName = teachersName;
    }

    @Override
    public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(Integer integer) {
            this.id=integer;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public Integer getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(Integer subgroup) {
        this.subgroup = subgroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeachersName() {
        return teachersName;
    }

    public void setTeachersName(String teachersName) {
        this.teachersName = teachersName;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group=" + group +
                ", subgroup=" + subgroup +
                ", email='" + email + '\'' +
                ", teachersName='" + teachersName + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
