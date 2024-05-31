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
        String directive = "";
        if(segmentType == SegmentType.CONSTANT){
            directive = "constant";
        }else if(segmentType == SegmentType.LOCAL){
            directive = "local";
        }
        else if(segmentType == SegmentType.ARG){
            directive = "argument";
        }
        if(directive.length()>0){
            writeFileInfo("push " + directive + " " + index);
        }else{
           throw new RuntimeException("SegmentType is null.");
        }

    }

    public void writePop(SegmentType segmentType, int index) {
        String directive = "";
        if(segmentType == SegmentType.CONSTANT){
            directive = "constant";
        }else if(segmentType == SegmentType.LOCAL){
            directive = "local";
        }
        else if(segmentType == SegmentType.ARG){
            directive = "argument";

        } else if(segmentType == SegmentType.TEMP){
            directive = "temp";
        }
        if(directive.length()>0){
            writeFileInfo("pop " + directive + " " + index);
        }else{
            throw new RuntimeException("SegmentType is null. "+segmentType);
        }
    }

    public void writeArithmetic(ArithmeticOperate operate) {
        writeFileInfo(operate.toString().toLowerCase());
    }

    public void writeLabel(String lable) {

        writeFileInfo("label "+lable);
    }

    public void writeGoto(String lable) {
        //goto IF_FALSE0
        writeFileInfo("goto "+lable);
    }

    public void writeIf(String lable) {
        //System.out.println("if-goto WHILE_END0");
        writeFileInfo("if-goto "+lable);
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
