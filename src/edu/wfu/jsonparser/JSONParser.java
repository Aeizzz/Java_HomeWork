package edu.wfu.jsonparser;

import edu.wfu.jsonparser.parser.Parser;
import edu.wfu.jsonparser.tokenizer.CharReader;
import edu.wfu.jsonparser.tokenizer.TokenList;
import edu.wfu.jsonparser.tokenizer.Tokenizer;

import java.io.StringReader;

public class JSONParser {

    private Tokenizer tokenizer = new Tokenizer();
    private Parser parser = new Parser();

    public Object parserJSON(String json) throws Exception {
        CharReader charReader = new CharReader(new StringReader(json));
        TokenList tokens = tokenizer.tokenize(charReader);
        return parser.parser(tokens);
    }
}
