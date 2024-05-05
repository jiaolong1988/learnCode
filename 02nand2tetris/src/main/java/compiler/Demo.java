package compiler;

import java.io.File;
import java.io.FileNotFoundException;

//https://github.com/AllenWrong/nand2tetris
public class Demo {
	public static void main(String[] args) {
		// change the file name
		File file = new File("D:\\test\\10\\Main.jack");
		File outputFile = new File("D:\\test\\10\\MainG.xml");
		if(outputFile.exists()) {
			outputFile.delete();
		}
		CompilationEngine compilationEngine = null;
		try {
			compilationEngine = new CompilationEngine(file, outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		compilationEngine.compileClass();
	}
}
