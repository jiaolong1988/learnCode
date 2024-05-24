package nand2tetris.compiler2.util;

/**
 * @author: jiaolong
 * @date: 2024/05/23 16:06
 **/
public class SymbolTableInfo {
    private String name;
    private String type;
    private String kind;
    private int index;

    public SymbolTableInfo(String name, String type, String kind) {
        this.name = name;
        this.type = type;
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
