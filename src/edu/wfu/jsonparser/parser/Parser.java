package edu.wfu.jsonparser.parser;

import edu.wfu.jsonparser.tokenizer.Token;
import edu.wfu.jsonparser.tokenizer.TokenList;
import edu.wfu.jsonparser.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    /**
     * BEGIN_OBJECT(1), {
     * END_OBJECT(2),   }
     * BEGIN_ARRAY(4), [
     * END_ARRAY(8), ]
     * NULL(16), null
     * NUMBER(32), 数字
     * STRING(64), string
     * BOOLEAN(128), tree false
     * SEP_COLON(256), :
     * SEP_COMMA(512),  ,
     * END_DOCUMENT(1024);
     */


    private static final int BEGIN_OBJECT_TOKEN = 1;
    private static final int END_OBJECT_TOKEN = 2;
    private static final int BEGIN_ARRAY_TOKEN = 4;
    private static final int END_ARRAY_TOKEN = 8;
    private static final int NULL_TOKEN = 16;
    private static final int NUMBER_TOKEN = 32;
    private static final int STRING_TOKEN = 64;
    private static final int BOOLEAN_TOKEN = 128;
    private static final int SEP_COLON_TOKEN = 256;
    private static final int SEP_COMMA_TOKEN = 512;
    private static final int END_DOCUMENT_TOKEN = 1024;

    private TokenList tokens;

    public Map<String, Object> parser(TokenList tokens) throws Exception {
        this.tokens = tokens;
        return parser();
    }


    private Map<String, Object> parser() throws Exception {
        Token token = tokens.next();
        if (token == null) {
            return new HashMap<String, Object>();
        } else if (token.getTokenType() == TokenType.BEGIN_OBJECT) {
            return parserJsonObject();
        } else {
            throw new Exception("错误");
        }
    }

    private List parserJsonArray() throws Exception {
        // 除了 :  和 ， d都可以
        int exp = BEGIN_ARRAY_TOKEN | END_ARRAY_TOKEN | BEGIN_OBJECT_TOKEN | NULL_TOKEN
                | NUMBER_TOKEN | BOOLEAN_TOKEN | STRING_TOKEN;

        List<Object> jsonArray = new ArrayList();
        while (tokens.hasMore()) {
            Token token = tokens.next();
            TokenType tokenType = token.getTokenType();
            String tokenValue = token.getValue();
            switch (tokenType) {
                //如果是对象类型话
                case BEGIN_OBJECT:
                    checkExpectToken(tokenType, exp);
                    // 递归解析Object
                    jsonArray.add(parserJsonObject());
                    //更新 object 后面只能是  ，  或者 ]
                    exp = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case BEGIN_ARRAY:
                    checkExpectToken(tokenType, exp);
                    jsonArray.add(parserJsonArray());
                    // 数组结束以后 为  ]  或者  ，
                    exp = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case END_ARRAY:
                    checkExpectToken(tokenType, exp);
                    return jsonArray;
                case NUMBER:
                    checkExpectToken(tokenType, exp);
                    Long num = Long.valueOf(tokenValue);
                    jsonArray.add(num);
                    exp = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case BOOLEAN:
                    checkExpectToken(tokenType, exp);
                    jsonArray.add(Boolean.valueOf(token.getValue()));
                    // 结束后下一个必为 }  或者 ，
                    exp = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case STRING:
                    checkExpectToken(tokenType, exp);
                    jsonArray.add(token.getValue());
                    exp = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case SEP_COMMA:
                    checkExpectToken(tokenType, exp);
                    // , 后面可以有的类型
                    exp = NULL_TOKEN | NUMBER_TOKEN | BOOLEAN_TOKEN | STRING_TOKEN | BEGIN_OBJECT_TOKEN | BEGIN_ARRAY_TOKEN;
                    break;
                case NULL:
                    checkExpectToken(tokenType, exp);
                    jsonArray.add(null);
                    exp = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case END_DOCUMENT:
                    checkExpectToken(tokenType, exp);
                    return jsonArray;
                default:
                    throw new Exception("未识别Token");


            }
        }
        throw new Exception("");
    }

    /**
     * 解析对象类型
     *
     * @return
     */
    private Map<String, Object> parserJsonObject() throws Exception {
//        JsonObject jsonObject = new JsonObject();
        Map<String, Object> jsonObject = new HashMap<>();
        int exp = STRING_TOKEN | END_OBJECT_TOKEN;
        String key = null;
        Object value = null;
        while (tokens.hasMore()) {
            Token token = tokens.next();
            // 获取类型
            TokenType tokenType = token.getTokenType();
            String tokenValue = token.getValue();
            switch (tokenType) {
                //如果是对象类型话
                case BEGIN_OBJECT:
                    checkExpectToken(tokenType, exp);
                    // 递归解析Object
                    jsonObject.put(key, parserJsonObject());
                    //更新 object 后面只能是  ，  或者 }
                    exp = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case END_OBJECT:
                    checkExpectToken(tokenType, exp);
                    return jsonObject;
                case BEGIN_ARRAY:
                    checkExpectToken(tokenType, exp);
                    jsonObject.put(key, parserJsonArray());
                    // 数组结束以后 为  }  或者  ，
                    exp = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case NULL:
                    checkExpectToken(tokenType, exp);
                    jsonObject.put(key, null);
                    exp = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                case NUMBER:
                    checkExpectToken(tokenType, exp);
                    Long num = Long.valueOf(tokenValue);
                    jsonObject.put(key, num);
                    exp = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case BOOLEAN:
                    checkExpectToken(tokenType, exp);
                    jsonObject.put(key, Boolean.valueOf(token.getValue()));
                    // 结束后下一个必为 }  或者 ，
                    exp = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case STRING:
                    checkExpectToken(tokenType, exp);
                    // 获取前一个token来判断这一个string  是  key  还是 value
                    Token preToken = tokens.peekPrevious();
                    // 若为 :  则它为 value
                    if (preToken.getTokenType() == TokenType.SEP_COLON) {
                        value = token.getValue();
                        jsonObject.put(key, value);
                        exp = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    } else {
                        key = token.getValue();
                        // 为 key 时 他的下一个必定为 :
                        exp = SEP_COLON_TOKEN;
                    }
                    break;
                case SEP_COLON:
                    checkExpectToken(tokenType, exp);
                    // : 后面可以有的类型
                    exp = NULL_TOKEN | NUMBER_TOKEN | BOOLEAN_TOKEN | STRING_TOKEN | BEGIN_OBJECT_TOKEN | BEGIN_ARRAY_TOKEN;
                    break;
                case SEP_COMMA:
                    checkExpectToken(tokenType, exp);
                    // ， 后面只能有key  也就时STRING 类型
                    exp = STRING_TOKEN;
                    break;
                case END_DOCUMENT:
                    checkExpectToken(tokenType, exp);
                    return jsonObject;
                default:
                    throw new Exception("未识别Token");

            }
        }
        throw new Exception("");
    }


    private void checkExpectToken(TokenType tokenType, int expectToken) throws Exception {
        if ((tokenType.getTokenCode() & expectToken) == 0) {
            throw new Exception("格式不正确" + "期望类型为" + expectToken);
        }
    }

}
