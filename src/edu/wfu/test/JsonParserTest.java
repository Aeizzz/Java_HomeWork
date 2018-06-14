package edu.wfu.test;

import edu.wfu.jsonparser.JSONParser;
import edu.wfu.jsonparser.tokenizer.CharReader;
import edu.wfu.jsonparser.tokenizer.TokenList;
import edu.wfu.jsonparser.tokenizer.Tokenizer;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

public class JsonParserTest {


    /**
     * 测试 读入
     * @throws IOException
     */
    @Test
    public void TestCharReader() throws IOException {
        String  json = "{\n" +
                "    \"tools\": [\n" +
                "    { \"name\":\"css format\" , \"site\":\"http://www.atool.org/csscompression.php\" },\n" +
                "    { \"name\":\"json format\" , \"site\":\"http://www.atool.org/jsonformat.php\" },\n" +
                "    { \"name\":\"hash MD5\" , \"site\":\"http://www.atool.org/hash.php\" }\n" +
                "    ]\n" +
                "    }";

        CharReader charReader = new CharReader(new StringReader(json));
        while (charReader.hasMore()){
            System.out.println(charReader.next());
        }

    }

    /**
     * 测试读入json 后token 的生成
     * @throws Exception
     */
    @Test
    public void TestTokenList() throws Exception {
        String  json = "{\n" +
                "    \"tools\": [\n" +
                "    { \"name\":\"css format\" , \"site\":\"http://www.atool.org/csscompression.php\" },\n" +
                "    { \"name\":\"json format\" , \"site\":\"http://www.atool.org/jsonformat.php\" },\n" +
                "    { \"name\":\"hash MD5\" , \"site\":\"http://www.atool.org/hash.php\" }\n" +
                "    ]\n" +
                "    }";

        CharReader charReader = new CharReader(new StringReader(json));


        Tokenizer tokenizer = new Tokenizer();
        TokenList tokens = tokenizer.tokenize(charReader);
        while (tokens.hasMore()){
            System.out.println(tokens.next());
        }


    }





    @Test
    public void Test1() throws Exception {
        String  json = "{\n" +
                "    \"tools\": [\n" +
                "    { \"name\":\"css format\" , \"site\":\"http://www.atool.org/csscompression.php\" },\n" +
                "    { \"name\":\"json format\" , \"site\":\"http://www.atool.org/jsonformat.php\" },\n" +
                "    { \"name\":\"hash MD5\" , \"site\":\"http://www.atool.org/hash.php\" }\n" +
                "    ]\n" +
                "    }";

        JSONParser jsonParser = new JSONParser();
        Map<String,Object> map = jsonParser.parserJSON(json);
        System.out.println(map);

    }


    @Test
    public void Test2() throws Exception {
        File file = new File("F:\\JavaProject\\Java_HomeWork\\src\\file\\json.json");
        String json = FileUtils.readFileToString(file,"UTF-8");
        JSONParser jsonParser = new JSONParser();
        Map<String,Object> map = jsonParser.parserJSON(json);
        System.out.println(map);

    }

}
