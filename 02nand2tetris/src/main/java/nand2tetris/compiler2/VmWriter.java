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
        String command = getSegmentTypeCommand(segmentType);
        writeFileInfo("push " + command + " " + index);
    }

    public void writePop(SegmentType segmentType, int index) {
        String command = getSegmentTypeCommand(segmentType);
        writeFileInfo("pop " + command + " " + index);
    }

    private String getSegmentTypeCommand(SegmentType segmentType) {

        String directive = "";
        if(segmentType == SegmentType.CONSTANT){
            directive = "constant";
        }else if(segmentType == SegmentType.ARG){
            directive = "argument";
        }
        else if(segmentType == SegmentType.LOCAL){
            directive = "local";
        }
        else if(segmentType == SegmentType.STATIC){
            directive = "static";
        }
        else if(segmentType == SegmentType.THIS){
            directive = "this";
        }
        else if(segmentType == SegmentType.THAT){
            directive = "that";
        }
        else if(segmentType == SegmentType.POINTER){
            directive = "pointer";
        }
        else if(segmentType == SegmentType.TEMP){
            directive = "temp";
        }
        if(directive.length()==0){
            throw new RuntimeException("SegmentType not have command. reason: "+segmentType.toString());
        }

        return directive;
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
