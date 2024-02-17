package nand2tetris.vm1;


public class Vm1ServerMain {
    public static void main(String[] args) {
        String file ="D:\\test\\07\\MemoryAccess\\StaticTest\\StaticTest.vm";
        Vm1ServerMain.ParserSingle(file);
    }
    /**
     * 单文件解析
     * @param: file
     * @return: void
     **/
    public static void ParserSingle(String file){
        //重置堆栈指针
        StackSPUtils.reset();

        Parser parser = new Parser(file);
        CodeWriter codeWriter = new CodeWriter(file);

        int lineNum=0;
        while(true){
            boolean flag = parser.advance();
            if(!flag ){
                break;
            }

            //vm 翻译为 汇编
            String arg1 = parser.arg1().trim();
            String arg2 = parser.arg2();

            //push 和 pop
            CmomandType currentCommantType = parser.commandType();
            if(currentCommantType == CmomandType.C_PUSH || currentCommantType == CmomandType.C_POP){
                codeWriter.writePushPop(parser.commandType(), arg1, arg2);
            }
            //算术类型指令
            if(currentCommantType == CmomandType.C_ARITHMETIC){
                codeWriter.writerArithmetic(arg1);
            }

            lineNum++;
            System.out.println("--> "+lineNum);
        }

        codeWriter.close();
    }
}
