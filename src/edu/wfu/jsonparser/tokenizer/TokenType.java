package edu.wfu.jsonparser.tokenizer;

/**
 * JSON 数据类型
 * 一共九种+文档结束
 */


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
