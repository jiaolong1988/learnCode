package nand2tetris.compiler1;

import nand2tetris.CommonUtils;
import nand2tetris.compiler1.pubField.Keyword;
import nand2tetris.compiler1.pubField.TokenType;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ��Ԫת����
 *
 * @author jiaolong
 * @date 2024/04/06 11:01
 */
public class JackTokenizer {

    private Iterator<String> iterator = null;
    //��ǰJack����
    private String currentCommand;

    public JackTokenizer(File inputFilePath) {
        //��ȡ�ļ�����
        List<String> jackInfo = CommonUtils.readFile(inputFilePath.toPath());

        //���ݹ���
        List<String> list = new ArrayList<>();
        for(String info :jackInfo){

            String command = "";
            if(info.contains("/**")){
                command = info.substring(0,info.indexOf("/**")).trim();

            }else if(info.contains("//")){
                command = info.substring(0,info.indexOf("//")).trim();
            }else{
                command = info.trim();
            }

            if(command.length()>0){
                list.add(command);
            }
        }
        iterator = list.iterator();
    }

    public boolean hasMoreTokens() {
        return iterator.hasNext();
    }

    //��ȡ��һ��ָ�����������Ϊ��ǰ����
    public boolean advance(){
        boolean flag = hasMoreTokens();
        if(flag){
            currentCommand = iterator.next().trim();
            System.out.println("jack info��"+currentCommand);
        }

        return flag;
    }

    public TokenType tokenType() {

        return TokenType.IDENTIFIER;
    }

    //��Ԫ��5������1
    public Keyword keyword() {
    //<keyword>  </keyword>

        return Keyword.BOOLEAN;
    }

    //����
    public String symbol() {
    //<symbol>  </symbol>

        return null;
    }

    //��ʶ�������� ������ ������
    public String identifier() {
    //<identifier>  </identifier>

        return null;
    }

    //��������
    public int inVal() {
    //<integerConstant>  </integerConstant>
        return 0;
    }

    //�ַ�����
    public String StringVal() {
        //<stringConstant> </stringConstant>
        return null;
    }

}
