package nand2tetris.compiler2;

import nand2tetris.compiler2.util.SegmentType;
import nand2tetris.compiler2.util.SymbolTableInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiaolong
 * @date: 2024/05/23 10:40
 **/
public class SymbolTable {

    //类级别
    Map<String, SymbolTableInfo> staticType = new HashMap<>();
    Map<String, SymbolTableInfo> field = new HashMap<>();
    //方法级别
    Map<String, SymbolTableInfo> var = new HashMap<>();
    Map<String, SymbolTableInfo> argument = new HashMap<>();

    private String subroutineName;
    private String subroutineReturnType;

    //开创新的子程序作用域（既将子程序的符号重置）
    public void startSubroutine(String subroutineReturnType,String subroutineName) {
        this.subroutineReturnType = subroutineReturnType;
        this.subroutineName = subroutineName;
        var.clear();
        argument.clear();
    }

    /*
       name:变量名称
       type:int String boolean
       kind：static field,arg,var
     */
    public void define(String name, String type, String kind) {
        SymbolTableInfo info = new SymbolTableInfo(type);
        if (kind.equals("static")) {
            info.setIndex(staticType.size());
            staticType.put(name, info);
        }

        if (kind.equals("field")) {
            info.setIndex(field.size());
            field.put(name, info);
        }

        if (kind.equals("arg")) {
            info.setIndex(argument.size());
            argument.put(name, info);
        }
        if (kind.equals("var")) {
            info.setIndex(var.size());
            var.put(name, info);
        }
    }

    //当前作用域内的变量数量
    public int varCount(String kind) {
        if (kind.equals("static")) {
           return staticType.size();
        }

        else if (kind.equals("field")) {
          return   field.size();
        }

       else if (kind.equals("arg")) {
           return argument.size();
        }
        else if (kind.equals("var")) {
            return var.size();
        }else{
            throw new RuntimeException("kind type error.");
        }
    }

    //返回当前作用域的 的标识符种类
    public SegmentType kindOf(String name) {
        SegmentType kind = null;
        if (var.containsKey(name)) {
            kind = SegmentType.LOCAL;
        }
       else if (argument.containsKey(name)) {
            kind = SegmentType.ARG;
        }
//       else if (field.containsKey(name)) {
//            kind = SegmentType.;
//
//        }
       else if (staticType.containsKey(name)) {
            kind = SegmentType.STATIC;
        }

        return kind;
    }

    public String typeOf(String name) {
        String info =null;
        if (var.containsKey(name)) {
            info = var.get(name).getType();
        }
        else if (argument.containsKey(name)) {
            info = argument.get(name).getType();
        }
        else if (staticType.containsKey(name)) {
            info = staticType.get(name).getType();
        }
        return info;
    }

    public int indexOf(String name) {
        int index = -1;
        if (var.containsKey(name)) {
            index = var.get(name).getIndex();
        } else if (argument.containsKey(name)) {
            index = argument.get(name).getIndex();
        } else if (field.containsKey(name)) {
            index = field.get(name).getIndex();
        } else if (staticType.containsKey(name)) {
            index = staticType.get(name).getIndex();
        }

        return index;
    }

    public String getSubroutineReturnType() {
        return subroutineReturnType;
    }

    public String getSubroutineName() {
        return subroutineName;
    }
}
