package edu.wfu.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.wfu.utils.JsonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToJsonTest {


    /**
     * 测试 alibaba fastjson
     */
    @Test
    public void Test() throws Exception {
        Map<String, Object> student = new HashMap<>();
        student.put("id", 123456);
        student.put("name", "zhansan");
        student.put("addr", "weifang");

        Map<String, Object> student2 = new HashMap<>();
        student2.put("id", 15324.635);
        student2.put("name", "77777");
        student2.put("addr", "weifang777");


        List<Map> studentList = new ArrayList<>();
        studentList.add(student);
        studentList.add(student2);


        Map<String, Object> map = new HashMap<>();
        map.put("id", 12354);
        map.put("name", "jike");
        map.put("studentList", studentList);

        String json = JsonUtils.toJSONString(map);
        System.out.println(json);


    }





}
