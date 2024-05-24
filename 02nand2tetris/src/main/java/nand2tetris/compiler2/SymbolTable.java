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

    //�༶��
    Map<String, SymbolTableInfo> staticType = new HashMap<>();
    Map<String, SymbolTableInfo> field = new HashMap<>();
    //��������
    Map<String, SymbolTableInfo> var = new HashMap<>();
    Map<String, SymbolTableInfo> argument = new HashMap<>();

    //�����µ��ӳ��������򣨼Ƚ��ӳ���ķ������ã�
    public void startSubroutine() {
    }

    /*
       name:��������
       type:int String boolean
       kind��static field,arg,var
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

    //��ǰ�������ڵı�������
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

    //���ص�ǰ������� �ı�ʶ������
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
