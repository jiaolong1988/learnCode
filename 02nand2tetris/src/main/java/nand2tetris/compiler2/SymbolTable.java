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

    //�༶��
    Map<String, SymbolTableInfo> staticType = new HashMap<>();
    Map<String, SymbolTableInfo> field = new HashMap<>();
    //��������
    Map<String, SymbolTableInfo> var = new HashMap<>();
    Map<String, SymbolTableInfo> argument = new HashMap<>();

    private String subroutineName;
    private String subroutineReturnType;

    //�����µ��ӳ��������򣨼Ƚ��ӳ���ķ������ã�
    public void startSubroutine(String subroutineReturnType,String subroutineName) {
        this.subroutineReturnType = subroutineReturnType;
        this.subroutineName = subroutineName;
        var.clear();
        argument.clear();
    }

    /*
       name:��������
       type:int String boolean
       kind��static field,arg,var
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
