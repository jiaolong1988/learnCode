package nand2tetris.assembler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiaolong
 * @date 2023/12/14 16:42
 */
public class InitData {
    public static Map<String, Integer> symbolTable = new HashMap<>();

    public static Map<String, String> compCommand =  new HashMap<>();
    public static Map<String, String> destCommand =  new HashMap<>();
    public static Map<String, String> jumpCommand =  new HashMap<>();
    static{

        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);

        compCommand.put("0","0101010"  );
        compCommand.put("1","0111111"  );
        compCommand.put("-1","0111010" );
        compCommand.put("D","0001100"  );
        compCommand.put("A","0110000"  );
        compCommand.put("!D","0001101" );
        compCommand.put( "!A","0110001");
        compCommand.put("-D","0001111" );
        compCommand.put("-A","0110011" );
        compCommand.put("D+1","0011111");
        compCommand.put("A+1","0110111");
        compCommand.put("D-1","0001110");
        compCommand.put("A-1","0110010");
        compCommand.put("D+A","0000010");
        compCommand.put("D-A","0010011");
        compCommand.put("A-D","0000111");
        compCommand.put("D&A","0000000");
        compCommand.put("D|A","0010101");
        compCommand.put("M","1110000"  );
        compCommand.put("!M","1110001" );
        compCommand.put("-M","1110011" );
        compCommand.put("M+1","1110111");
        compCommand.put("M-1","1110010");
        compCommand.put("D+M","1000010");
        compCommand.put("D-M","1010011");
        compCommand.put("M-D","1000111");
        compCommand.put("D&M","1000000");
        compCommand.put("D|M","1010101");

        destCommand.put("null","000"  );
        destCommand.put("M","001"     );
        destCommand.put("D","010"     );
        destCommand.put("MD","011"    );
        destCommand.put("A","100"     );
        destCommand.put("AM","101"    );
        destCommand.put("AD","110"    );
        destCommand.put("AMD","111"   );

        jumpCommand.put("null","000" );
        jumpCommand.put("JGT","001"  );
        jumpCommand.put("JEQ","010"  );
        jumpCommand.put("JGE","011"  );
        jumpCommand.put("JLT","100"  );
        jumpCommand.put("JNE","101"  );
        jumpCommand.put("JLE","110"  );
        jumpCommand.put("JMP","111"  );

    }
}
