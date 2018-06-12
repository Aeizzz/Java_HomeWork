package edu.wfu.test;

import edu.wfu.bean.Clazz;
import edu.wfu.bean.Student;
import edu.wfu.bean.Zlass;
import edu.wfu.utils.BeanUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ToBeanTest {


    @Test
    public void Test1() throws Exception {

        Map<String, Object> map = new HashMap<>();
        Student student = new Student();

        student.setName("张三");
        student.setAddr("潍坊");
        student.setId("96451");


        map.put("id", "19");
        map.put("name", "计科");
        map.put("student", student);

        Zlass clazz = new Zlass();


        BeanUtils.copyProperties(map, clazz);

        System.out.println(clazz);

    }


    @Test
    public void Test2() throws Exception {

        Student student = new Student();

        student.setName("张三");
        student.setAddr("潍坊");
        student.setId("1996");


        Zlass zlass = new Zlass();
        zlass.setId("1522222");
        zlass.setName("计科");
        zlass.setStudent(student);

        Clazz clazz = new Clazz();

        BeanUtils.copyProperties(zlass, clazz);

        System.out.println(zlass);

        System.out.println(clazz);


    }


    @Test
    public void Test3() throws Exception {
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> student = new HashMap<>();
        student.put("id", "65535");
        student.put("name", "张三");
        student.put("addr", "潍坊");

        map.put("id", "18");
        map.put("name", "计科");
        map.put("student", student);

        Zlass zlass = new Zlass();



        BeanUtils.copyProperties(map, zlass);



        System.out.println(zlass);


    }
}
