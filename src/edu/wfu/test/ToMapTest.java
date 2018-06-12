package edu.wfu.test;

import edu.wfu.bean.Student;
import edu.wfu.bean.Zlass;
import edu.wfu.utils.BeanUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
}
