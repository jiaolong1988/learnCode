package nand2tetris.compiler2.util;

/**
 * @author: jiaolong
 * @date: 2024/05/23 16:06
 **/
public class SymbolTableInfo {
    private String type;
    private int index;

    public SymbolTableInfo(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
