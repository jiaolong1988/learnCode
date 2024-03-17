package nand2tetris.vm2;


import nand2tetris.vm1.CmomandType;
import nand2tetris.vm1.StackSPUtils;

/**
 * @author jiaolong
 * @date 2024/03/10 13:31
 */
public class Vm2ServerMain {
    public static void main(String[] args) {
        String file ="D:\\test\\08\\FunctionCalls\\SimpleFunction\\SimpleFunction.vm";
        ParserSingle(file);

    }
    /**
     * ���ļ�����
     * @param: file
     * @return: void
     **/
    public static void ParserSingle(String file){
        //���ö�ջָ��
        StackSPUtils.reset();

       Parser parser = new Parser(file);
       CodeWriter codeWriter = new CodeWriter(file);

        int lineNum=0;
        while(true){
            boolean flag = parser.advance();
            if(!flag ){
                break;
            }

            CmomandType currentCommantType = parser.commandType();
            if(currentCommantType == CmomandType.C_RETURNN){
                codeWriter.wirteReturn();

            }else{
                //vm ����Ϊ ���
                String arg1 = parser.arg1().trim();
                String arg2 = parser.arg2();

                //push �� pop
                if(currentCommantType == CmomandType.C_PUSH || currentCommantType == CmomandType.C_POP){
                    codeWriter.writePushPop(parser.commandType(), arg1, arg2);
                }
                //��������ָ��
                if(currentCommantType == CmomandType.C_ARITHMETIC){
                    codeWriter.writerArithmetic(arg1);
                }
                //���̿���ָ��
                if(currentCommantType == CmomandType.C_LABLE ||
                        currentCommantType == CmomandType.C_IF ||
                        currentCommantType == CmomandType.C_GOTO){
                    codeWriter.writeFlow(currentCommantType, arg1);
                }
                if(currentCommantType == CmomandType.C_FUNCTION){
                    codeWriter.wirteFunction(arg1, Integer.valueOf(arg2));
                }
            }

            lineNum++;
            System.out.println("--> "+lineNum);
        }

        codeWriter.close();
    }



}
