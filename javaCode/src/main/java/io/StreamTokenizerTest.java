package io;

import java.io.*;
import java.util.Hashtable;

/**
 * https://blog.csdn.net/cy973071263/article/details/105588082
 * https://www.runoob.com/manual/jdk11api/java.base/java/io/StreamTokenizer.html
 * @author: jiaolong
 * @date: 2024/05/23 9:52
 **/
public class StreamTokenizerTest {
    public static void main(String[] args) throws IOException {
        System.out.println("45: "+(char)45);
        System.out.println("46: "+(char)46);
        System.out.println("60: "+(char)60);
        System.out.println("62: "+(char)62);
        System.out.println("47: "+(char)47);
        System.out.println("95: "+(char)95);
        System.out.println("255: "+(char)255);


        File paramFile = new File("./javaCode/Main.jack");
        FileReader fr =  new FileReader(paramFile.getCanonicalFile());
        StreamTokenizerTest streamTokenizerTest = new StreamTokenizerTest(fr);
        streamTokenizerTest.advance();
//
//        System.out.println(streamTokenizerTest.getTokenType());
//        System.out.println(streamTokenizerTest.getKeywordType());
    }

    public static final int TYPE_KEYWORD = 1;

    public static final int TYPE_SYMBOL = 2;

    public static final int TYPE_IDENTIFIER = 3;

    public static final int TYPE_INT_CONST = 4;

    public static final int TYPE_STRING_CONST = 5;

    private StreamTokenizer parser;

    private Hashtable keywords;

    private Hashtable symbols;

    private int tokenType;

    private int keyWordType;

    private char symbol;

    private int intValue;

    private int lineNumber;

    private String stringValue;

    private String identifier;

    public StreamTokenizerTest(Reader paramReader) {
        try {
            this.parser = new StreamTokenizer(paramReader);
            //解析数字
            this.parser.parseNumbers();
            //解析注释
            this.parser.slashSlashComments(true);
            this.parser.slashStarComments(true);

            //指定字符参数在此分词器中为“普通”
            this.parser.ordinaryChar(46);
            this.parser.ordinaryChar(45);
            this.parser.ordinaryChar(60);
            this.parser.ordinaryChar(62);
            this.parser.ordinaryChar(47);

            //设置单词有哪些字符
            this.parser.wordChars(95, 95);
            this.parser.nextToken();
            initKeywords();
            initSymbols();
        } catch (IOException iOException) {
            System.out.println("JackTokenizer failed during initialization operation");
            System.exit(-1);
        }
    }

    public void advance() {
        try {
            String str;
            switch (this.parser.ttype) {
                case -2:
                    this.tokenType = 4;
                    this.intValue = (int)this.parser.nval;
                    break;
                case -3:
                    str = this.parser.sval;
                    if (this.keywords.containsKey(str)) {
                        this.tokenType = 1;
                        this.keyWordType = ((Integer)this.keywords.get(str)).intValue();
                        break;
                    }
                    this.tokenType = 3;
                    this.identifier = str;
                    break;
                case 34:
                    this.tokenType = 5;
                    this.stringValue = this.parser.sval;
                    break;
                default:
                    this.tokenType = 2;
                    this.symbol = (char)this.parser.ttype;
                    break;
            }
            this.lineNumber = this.parser.lineno();
            this.parser.nextToken();
        } catch (IOException iOException) {
            System.out.println("JackTokenizer failed during advance operation");
            System.exit(-1);
        }
    }

    public int getTokenType() {
        return this.tokenType;
    }

    public int getKeywordType() {
        return this.keyWordType;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public int getIntValue() {
        return this.intValue;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public boolean hasMoreTokens() {
        return (this.parser.ttype != -1);
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    private void initKeywords() {
        this.keywords = new Hashtable();
        this.keywords.put("class", new Integer(1));
        this.keywords.put("method", new Integer(2));
        this.keywords.put("function", new Integer(3));
        this.keywords.put("constructor", new Integer(4));
        this.keywords.put("int", new Integer(5));
        this.keywords.put("boolean", new Integer(6));
        this.keywords.put("char", new Integer(7));
        this.keywords.put("void", new Integer(8));
        this.keywords.put("var", new Integer(9));
        this.keywords.put("static", new Integer(10));
        this.keywords.put("field", new Integer(11));
        this.keywords.put("let", new Integer(12));
        this.keywords.put("do", new Integer(13));
        this.keywords.put("if", new Integer(14));
        this.keywords.put("else", new Integer(15));
        this.keywords.put("while", new Integer(16));
        this.keywords.put("return", new Integer(17));
        this.keywords.put("true", new Integer(18));
        this.keywords.put("false", new Integer(19));
        this.keywords.put("null", new Integer(20));
        this.keywords.put("this", new Integer(21));
    }

    private void initSymbols() {
        this.symbols = new Hashtable();
        this.symbols.put("(", "(");
        this.symbols.put(")", ")");
        this.symbols.put("[", "[");
        this.symbols.put("]", "]");
        this.symbols.put("{", "{");
        this.symbols.put("}", "}");
        this.symbols.put(",", ",");
        this.symbols.put(";", ";");
        this.symbols.put("=", "=");
        this.symbols.put(".", ".");
        this.symbols.put("+", "+");
        this.symbols.put("-", "-");
        this.symbols.put("*", "*");
        this.symbols.put("/", "/");
        this.symbols.put("&", "&");
        this.symbols.put("|", "|");
        this.symbols.put("~", "~");
        this.symbols.put("<", "<");
        this.symbols.put(">", ">");
    }
}
