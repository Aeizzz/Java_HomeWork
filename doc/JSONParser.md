## 背景介绍
JSON(JavaScript Object Notation) 是一种轻量级的数据交换格式。相对于另一种数据交换格式 XML，JSON 有着诸多优点。比如易读性更好，占用空间更少等

## JSON 解析器实现原理
JSON 解析器从本质上来说就是根据 JSON 文法规则创建的状态机，输入是一个 JSON 字符串，输出是一个 JSON 对象。一般来说，解析过程包括词法分析和语法分析两个阶段。词法分析阶段的目标是按照构词规则将 JSON 字符串解析成 Token 流，比如有如下的 JSON 字符串：
```json
{
    "name" : "小明",
    "age": 18
}
```
结果词法分析后，得到一组 Token，如下：  
`{`、 `name`、 `:`、 `小明`、 `,`、 `age`、 `:`、 `18`、 `}`

此法分析解析出Token 序列后，然后进行语法分析。语法分析目的是根据JSON 语法检查上面的Token 序列所构成的JSON结构是否合法。
比如 JSON 文法要求非空 JSON 对象以键值对的形式出现，形如 object = {string : value}。如果传入了一个格式错误的字符串，比如
```json
{
    "name", "小明"
}
```
那么在语法分析阶段，语法分析器分析完 Token name后，认为它是一个符合规则的 Token，并且认为它是一个键。接下来，语法分析器读取下一个 Token，期望这个 Token 是 `:`。但当它读取了这个 Token，发现这个 Token 是 `,`，并非其期望的:，于是文法分析器就会报错误。


总结以上上个流程就是，词法分析是将字符串解析成一组Token，语法分析是检查输入的Toekn 所构成的Json是否合法


## 词法分析
我们参考http://www.json.org/ 对JSON的定义们可以得到JSON所规定的数据结构
 - BEGIN_OBJECT（{）
 - END_OBJECT（}）
 - BEGIN_ARRAY（[）
 - END_ARRAY（]）
 - NULL（null）
 - NUMBER（数字）
 - STRING（字符串）
 - BOOLEAN（true/false）
 - SEP_COLON（:）
 - SEP_COMMA（,）
 
当词法分析器读取的词是上面类型中的一种时，即可将其解析成一个 Token。
这样我们就可以定义一个枚举类来表示上面的数据类型
```java
public enum TokenType {
    // {
    BEGIN_OBJECT(1),
    // }
    END_OBJECT(2),
    // [
    BEGIN_ARRAY(4),
    // ]
    END_ARRAY(8),
    // null
    NULL(16),
    // 数字类型
    NUMBER(32),
    // string
    STRING(64),
    // true false
    BOOLEAN(128),
    // :
    SEP_COLON(256),
    // ,
    SEP_COMMA(512),
    // 文档结束
    END_DOCUMENT(1024);

    public int code;

    TokenType(int code) {
        this.code = code;
    }

    public int getTokenCode() {
        return code;
    }
}
``` 
再解析的过程中，我们不仅保存TokenType  还要保存这个词的值
```java
public class Token {
    // Token 类型
    private TokenType tokenType;
    // 所对应的值
    private String value;
}
```
定义好Token 类以后，再要一个读取字符串的类
```java
public class CharReader {
    private static final int BUFFER_SIZE = 1024;

    private Reader reader;
    private char[] buffer;
    private int pos;
    private int size;


    /**
     * 输入流
     * 每次读取1024 * 2个字节
     *
     * @param reader
     */
    public CharReader(Reader reader) {
        this.reader = reader;
        buffer = new char[BUFFER_SIZE];
    }

    // 弹出当前字符
    public char peek() {
        if (pos - 1 >= size) {
            return (char) -1;
        }
        return buffer[Math.max(0, pos - 1)];
    }

    // 回退一个
    public void back() {
        pos = Math.max(0, --pos);
    }

    public char next() throws IOException {
        if (!hasMore()) {
            return (char) -1;
        }
        return buffer[pos++];
    }


    // 判断是否还有
    public boolean hasMore() throws IOException {
        if (pos < size) {
            return true;
        }
        fillBuffer();
        return pos < size;
    }

    // 输入读入
    private void fillBuffer() throws IOException {
        int n = reader.read(buffer);
        if (n == -1) {
            return;
        }

        pos = 0;
        size = n;
    }

}

```

有了上面的所有类，就可以来完成词法分析了。
```java
public class Tokenizer {
    private CharReader charReader;
    private TokenList tokens;

    public TokenList tokenize(CharReader charReader) throws Exception {
        this.charReader = charReader;
        tokens = new TokenList();
        tokenizer();
        return tokens;
    }

    public void tokenizer() throws Exception {
        Token token;
        do {
            // 每次解析出一个token 返回增加到list中，直达文档结束
            token = start();
            tokens.add(token);
        } while (token.getTokenType() != TokenType.END_DOCUMENT);
    }

    /**
     * 开始解析
     *
     * @return
     */
    private Token start() throws Exception {
        char ch;
        while (true) {
            if (!charReader.hasMore()) {
                return new Token(TokenType.END_DOCUMENT, null);
            }

            ch = charReader.next();
            if (!isWhiteSpace(ch)) {
                break;
            }
        }

        /**
         * 判断Token 类型
         */
        switch (ch) {
            case '{':
                return new Token(TokenType.BEGIN_OBJECT, "{");
            case '}':
                return new Token(TokenType.END_OBJECT, "}");
            case '[':
                return new Token(TokenType.BEGIN_ARRAY, "[");
            case ']':
                return new Token(TokenType.END_ARRAY, "]");
            case ':':
                return new Token(TokenType.SEP_COLON, ":");
            case ',':
                return new Token(TokenType.SEP_COMMA, ",");
            case 'n':
                return readNull();
            case 't':
            case 'f':
                return readBoolean();
            case '"':
                return readString();
            case '-':
                return readNumber();
            default:
        }

        if (isDigit(ch)) {
            return readNumber();
        }

        throw new Exception("发现json存在位置类型数据");

    }
    
    // 先忽略
    private Token readNull() {...}
    private Token readBoolean() {...}
    private Token readString() {...}
    private Token readNumber() {...}
    }
```

对于词法分析主要再Start函数中
```java
private Token start() throws Exception {
    char ch;
    while (true) {
        if (!charReader.hasMore()) {
            return new Token(TokenType.END_DOCUMENT, null);
        }

        ch = charReader.next();
        if (!isWhiteSpace(ch)) {
            break;
        }
    }

    /**
     * 判断Token 类型
     */
    switch (ch) {
        case '{':
            return new Token(TokenType.BEGIN_OBJECT, "{");
        case '}':
            return new Token(TokenType.END_OBJECT, "}");
        case '[':
            return new Token(TokenType.BEGIN_ARRAY, "[");
        case ']':
            return new Token(TokenType.END_ARRAY, "]");
        case ':':
            return new Token(TokenType.SEP_COLON, ":");
        case ',':
            return new Token(TokenType.SEP_COMMA, ",");
        case 'n':
            return readNull();
        case 't':
        case 'f':
            return readBoolean();
        case '"':
            return readString();
        case '-':
            return readNumber();
        default:
    }

    if (isDigit(ch)) {
        return readNumber();
    }

    throw new Exception("发现json存在位置类型数据");

}
```

使用死循环读入字符，然后根据字符来判断它属于哪个类型
 - 第一个字符是 ` { `  ,  ` } ` , ` [ ` , ` ] `  , ` ,` ,  ` :` 直接封装成相应的 Token 返回即可
 - 第一个字符是`n` ,则可以判断是不是null
 - 第一个字符是`t`,`f` 是来判断是不是true  或者false,是boolean类型
 - 第一个字符是`"`, 则可以判断是字符串，
 - 第一个字符是`0~9` 或者是 `-` 时则判断时不是数字类型
 
正如上面所说词法分析器只需要根据每一词的第一个字符来判断他的类型时。
如果符合则返回相应的类型，否则抛出异常

例如
```java
private Token readNull() throws Exception {
        if (!(charReader.next() == 'u' && charReader.next() == 'l' && charReader.next() == 'l')) {
            throw new Exception("json格式有误");
        }
        return new Token(TokenType.NULL, "null");
    }
```
这是判断 是否为null,这个判断很简单。看一下判断字符串的
```java
private Token readString() throws Exception {
    StringBuilder sb = new StringBuilder();
    while (true) {
        char ch = charReader.next();
        if (ch == '\\') {
            if (!isEsxape()) {
                throw new Exception("json 格式有错");
            }
            sb.append('\\');
            ch = charReader.peek();
            sb.append(ch);


        } else if (ch == '"') {
            return new Token(TokenType.STRING, sb.toString());
        } else if (ch == '\r' || ch == '\n') {
            throw new Exception("类型不对");
        } else {
            sb.append(ch);
        }
    }

}

public boolean isEsxape() throws IOException {
        char ch = charReader.next();
        return (ch == '"' || ch == '\\' || ch == 'u' || ch == 'r'
                || ch == 'n' || ch == 'b' || ch == 't' || ch == 'f');

}

```

String 类型稍微复杂一些，有一些特殊字符
```
\"
\\
\b
\f
\n
\r
\t
\u four-hex-digits
\/
```


## 语法分析
当词法分析结束以后，并且分析的过程中没有出错，解析来就进行语法分析。
语法分析实现语法如下
```java
object = {} | { members }
members = pair | pair , members
pair = string : value
array = [] | [ elements ]
elements = value  | value , elements
value = string | number | object | array | true | false | null
```

语法分析封装核心在 `parserJsonObject`这个函数中
```java
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
```

parseJsonObject 方法解析流程大致如下：
 1. 读取一个Token，检查这个Token是否时所期望的类型
 2. 如果是更新期望的Toekn类型。否则抛出异常
 3. 重复步骤1 和 2 直到所有的Token都解析完


上面步骤并不复杂，举例说明

`{`、 `id`、 `:`、 `1`、 `}`

我们知道 第一个Token是`{` 时 ,我们期望下一个是 STRING 类型的 Token 或者 END_OBJECT 类型的Token 出现

特殊的地方
STRING 类型在JSON中可以做值 也可以做为键，在判断的时候要判断前一个token，
如果是：  则为值，如果不是则为键````