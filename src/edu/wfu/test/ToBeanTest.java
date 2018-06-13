package edu.wfu.test;

import edu.wfu.bean.Classy;
import edu.wfu.bean.Clazz;
import edu.wfu.bean.Student;
import edu.wfu.bean.Zlass;
import edu.wfu.utils.BeanUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToBeanTest {


    /**
     * 测试map 转 bean  最简单类型
     *
     * @throws Exception
     */
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


    /**
     * 测试 bean  和 bean 的复制 ，相同属性名称
     *
     * @throws Exception
     */
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


    /**
     * 测试 map转 bean  嵌套 map
     *
     * @throws Exception
     */
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


    /**
     * 测试 map 转 bean  带有 list
     *
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        Map<String, Object> student = new HashMap<>();
        student.put("id", "12456");
        student.put("name", "zhansan");
        student.put("addr", "weifang");

        Map<String, Object> student2 = new HashMap<>();
        student2.put("id", "12456777");
        student2.put("name", "77777");
        student2.put("addr", "weifang777");


        List<Map> studentList = new ArrayList<>();
        studentList.add(student);
        studentList.add(student2);


        Map<String, Object> map = new HashMap<>();
        map.put("id", "123");
        map.put("name", "jike");
        map.put("studentList", studentList);


        Classy classy = new Classy();


        BeanUtils.copyProperties(map, classy);

        System.out.println(classy);


    }
}
