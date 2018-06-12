package edu.wfu.test;

import edu.wfu.bean.Student;

public class Json {

    public static void main(String[] args) throws Exception {
        Student student = new Student();
        student.setName("王思");
        student.setAddr("潍坊");
        student.setId(Long.valueOf(1996));


        Student st = new Student();
        edu.wfu.utils.BeanUtils.copyProperties(student, st);
        System.out.println(st);


    }
}
