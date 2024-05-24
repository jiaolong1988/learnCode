package nand2tetris.compiler2;

import nand2tetris.compiler2.util.SymbolTableInfo;
import nand2tetris.vm2Pro.ReturnAddressValueUtil;

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

    //开创新的子程序作用域（既将子程序的符号重置）
    public void startSubroutine() {
    }

    /*
       name:变量名称
       type:int String boolean
       kind：static field,arg,var
     */
    public void define(String name, String type, String kind) {
        SymbolTableInfo info = new SymbolTableInfo(name, type, kind);
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
    public String kindOf(String name) {
        return null;
    }

    public String typeOf(String name) {
        return null;
    }

    public int indexOf(String name) {
        return 0;
    }
}
