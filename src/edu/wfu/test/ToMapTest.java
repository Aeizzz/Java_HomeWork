package edu.wfu.test;

import edu.wfu.bean.Classy;
import edu.wfu.bean.Student;
import edu.wfu.bean.Zlass;
import edu.wfu.utils.BeanUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ToMapTest {


    @Test
    public void Test1() throws Exception {
        Student student = new Student();
        student.setId("1996");
        student.setAddr("潍坊");
        student.setName("张三");

        Zlass clazz = new Zlass();
        clazz.setStudent(student);
        clazz.setName("计科");
        clazz.setId("1502");

        Map<String, Object> map = new HashMap<>();
        BeanUtils.copyProperties(clazz, map);

        System.out.println(map);

    }

    @Test
    public void Test2() throws Exception {
        Classy classy = new Classy();
        classy.setId("199996");
        classy.setName("计科");

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("zhangsan","12312","潍坊"));
        studentList.add(new Student("王五","34534","潍坊"));
        studentList.add(new Student("asdasd","5675","asdssss"));
        studentList.add(new Student("aSdasd","11111","asd"));

        classy.setStudentList(studentList);

        Map<String,Object> map = new HashMap<>();
        BeanUtils.copyProperties(classy,map);
        System.out.println(map);


    }

}
