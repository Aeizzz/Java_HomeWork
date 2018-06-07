package edu.wfu.jsonparser.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储所有Token
 * 对数组进行操作
 * 1. 增加一个Token
 * 2. 获取token
 * 3. 获取前一个token
 * 4. 判断是否结束
 */


public class TokenList {
    // 保存全部token
    private List<Token> tokens = new ArrayList<Token>();
    // 记录当前token 遍历的位置
    private int pos = 0;


    // 增加token
    public void add(Token token) {
        tokens.add(token);
    }

    // 获取当前token
    public Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    public Token peekPrevious() {
        return pos - 1 < 0 ? null : tokens.get(pos - 2);
    }

    // 获取token 并且 下标指向下一个
    public Token next() {
        return tokens.get(pos++);
    }

    //  判断还有没有
    public boolean hasMore() {
        return pos < tokens.size();
    }
}
