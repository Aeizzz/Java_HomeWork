package edu.wfu.test;

import edu.wfu.jsonparser.JSONParser;
import edu.wfu.jsonparser.model.JsonObject;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Json {

    public static void main(String[] args) {
        String json = "{\n" +
                "\t\"animals\":{\n" +
                "\t\"dog\":[\n" +
                "\t\t{\"name\":\"Rufus\",\"breed\":\"labrador\",\"count\":1,\"twoFeet\":false},\n" +
                "\t\t{\"name\":\"Marty\",\"breed\":\"whippet\",\"count\":1,\"twoFeet\":false},\"asd\",\"asdasd\",\"44444\"\n" +
                "\t],\n" +
                "\t\"cat\":{\"name\":\"Matilda\"}\n" +
                "}\n" +
                "}";

        JSONParser jsonParser = new JSONParser();
        try {
            JsonObject map = (JsonObject) jsonParser.parserJSON(json);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
