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
    PrintWriter wirter;

    VmWriter(FileWriter fileWriter) {
        wirter = new PrintWriter(fileWriter, true);
    }

    public void writePush(SegmentType segmentType, int index) {
        writeFileInfo("push " + segmentType.toString().toLowerCase() + " " + index);
    }

    public void writePop(SegmentType segmentType, int index) {
        writeFileInfo("pop " + segmentType.toString().toLowerCase() + " " + index);
    }

    public void writeArithmetic(ArithmeticOperate operate) {
        writeFileInfo( ArithmeticOperate.NEG.toString().toLowerCase());
    }

    public void writeLabel(String lable) {
    }

    public void writeGoto(String lable) {
    }

    public void writeIf(String lable) {
    }

    public void writeCall(String methodName, int num) {
        writeFileInfo("call " + methodName + " " + num);
    }

    public void writeFunction(String methodName, int num) {
        writeFileInfo("function " + methodName + " " + num);
    }

    public void writeReturn() {
        writeFileInfo("return");
    }

    public void writeFileInfo(String writeInfo){
        wirter.println(writeInfo);
        System.out.println(writeInfo);
    }
    public void close() {
        wirter.close();
    }

}
