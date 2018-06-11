package edu.wfu.jsonparser.tokenizer;


import java.io.IOException;

/**
 * 词法解析器
 */
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

    /**
     * 读取字符串
     *
     * @return
     */
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

    /**
     * 读取数字类型
     * 现在只支持正负整数
     *
     * @return
     */
    private Token readNumber() throws IOException {
        char ch = charReader.peek();
        StringBuilder sb = new StringBuilder();
        // 处理负数
        if (ch == '-') {

            do {
                sb.append(ch);
                ch = charReader.next();
            } while (isDigit(ch));

        } else {
            do {
                sb.append(ch);
                ch = charReader.next();
            } while (isDigit(ch));
        }
        //读取结束  回退
        charReader.back();

        return new Token(TokenType.NUMBER, sb.toString());
    }

    private Token readBoolean() throws Exception {
        if (charReader.peek() == 't') {
            if (!(charReader.next() == 'r' && charReader.next() == 'u' && charReader.next() == 'e')) {
                throw new Exception("json格式有误");
            }
            return new Token(TokenType.BOOLEAN, "true");
        } else {
            if (!(charReader.next() == 'a' && charReader.next() == 'l'
                    && charReader.next() == 's' && charReader.next() == 'e')) {
                throw new Exception("json格式有误");
            }
            return new Token(TokenType.BOOLEAN, "false");
        }

    }


    /**
     * 判断null
     *
     * @return
     * @throws Exception
     */
    private Token readNull() throws Exception {
        if (!(charReader.next() == 'u' && charReader.next() == 'l' && charReader.next() == 'l')) {
            throw new Exception("json格式有误");
        }
        return new Token(TokenType.NULL, "null");
    }

    // 判断是否是数字
    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * 判断空白字符
     *
     * @param ch
     * @return
     */
    private boolean isWhiteSpace(char ch) {
        return (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r');
    }


    /**
     * 判断字符串中的转义
     * \"
     * \\
     * \b
     * \f
     * \n   ***
     * \r
     *
     * @return
     * @throws IOException
     */
    public boolean isEsxape() throws IOException {
        char ch = charReader.next();
        return (ch == '"' || ch == '\\' || ch == 'u' || ch == 'r'
                || ch == 'n' || ch == 'b' || ch == 't' || ch == 'f');

    }
}
