package edu.wfu.jsonparser.tokenizer;


/**
 * 每个token 对应类型和值
 */
public class Token {
    // Token 类型
    private TokenType tokenType;
    // 所对应的值
    private String value;

    public Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public Token() {
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", value='" + value + '\'' +
                '}';
    }
}
