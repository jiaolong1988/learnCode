package nand2tetris.compiler2;

import nand2tetris.compiler2.util.ArithmeticOperate;
import nand2tetris.compiler2.util.SegmentType;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * @author: jiaolong
 * @date: 2024/05/23 16:24
 **/
public class VmWriter {
    PrintWriter wirter ;
    VmWriter(FileWriter fileWriter){
        wirter= new PrintWriter(fileWriter, true);
    }

    public void writePush(SegmentType segmentType, int index) {
        if(segmentType == SegmentType.CONST){
            wirter.println("push constant "+index);
           // System.out.println("push constant "+index);
        }

    }
    public void writePop(SegmentType segmentType, int index) {
        if(segmentType == SegmentType.TEMP){
            wirter.println("pop temp "+index);
           // System.out.println("pop temp "+index);
        }
    }

    public void writeArithmetic(ArithmeticOperate operate) {
        if(operate==ArithmeticOperate.ADD){
            wirter.println("add");
            //System.out.println("add");
        }
    }

    public void writeLabel(String lable) {
    }

    public void writeGoto(String lable) {
    }

    public void writeIf(String lable) {
    }

    public void writeCall(String methodName, int num) {
        wirter.println("call "+methodName+" "+num);
        //System.out.println("call "+methodName+" "+num);
    }


    public void writeFunction(String methodName, int num) {
        wirter.println("function "+methodName+" "+num);
        //System.out.println("function "+methodName+" "+num);
    }

    public void writeReturn() {
        wirter.println("return");
       // System.out.println("return");
    }

    public void close(){
        wirter.close();
    }

}
