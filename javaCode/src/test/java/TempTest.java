import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiaolong
 * @date: 2024/06/24 16:45
 **/
public class TempTest {

    public static void main (String [] argv) throws Exception
    {

        System.out.println("sub main");
        new Sub();
//
//        ByteBuffer byteBuffer =
//                ByteBuffer.allocate (7).order (ByteOrder.BIG_ENDIAN);
//        CharBuffer charBuffer = byteBuffer.asCharBuffer( );
//        // Load the ByteBuffer with some bytes
//        byteBuffer.put (0, (byte)0);
//        byteBuffer.put (1, (byte)'H');
//        byteBuffer.put (2, (byte)0);
//        byteBuffer.put (3, (byte)'i');
//        byteBuffer.put (4, (byte)0);
//        byteBuffer.put (5, (byte)'!');
//        byteBuffer.put (6, (byte)0);
//        println (byteBuffer);
//        println (charBuffer);
    }
    // Print info about a buffer
    private static void println (Buffer buffer)
    {
        System.out.println ("pos=" + buffer.position( )
                + ", limit=" + buffer.limit( )
                + ", capacity=" + buffer.capacity( )
                + ": '" + buffer.toString( ) + "'");
    }


}
