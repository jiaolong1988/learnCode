package io.nio;

import java.nio.ByteBuffer;

/**
 * @author: jiaolong
 * @date: 2024/08/08 17:51
 **/
public class ScatterGatherTest {
    public static void main(String[] args) {

        short TYPE_FILE=111;

        ByteBuffer header = ByteBuffer.allocateDirect (10);
        ByteBuffer body = ByteBuffer.allocateDirect (80);
        ByteBuffer [] buffers = { header, body };
//        int bytesRead = channel.read (buffers);

     //   header.putShort (TYPE_FILE);
        header.putShort((short)222).putShort (TYPE_FILE).flip();

        System.out.println(header.getShort(0));


        System.out.println(header.getShort(1));



   //    long l = header.getLong(2);

//        switch (header.getShort(0)) {
//            case TYPE_PING:
//                break;
//            case TYPE_FILE:
//                body.flip( );
//                fileChannel.write (body);
//                break;
//            default:
//                logUnknownPacket (header.getShort(0), header.getLong(2), body);
//                break;
//        }

//        body.clear( );
//        body.put("FOO".getBytes()).flip( ); // "FOO" as bytes
//        header.clear( );
//        header.putShort (TYPE_FILE).putLong (body.limit()).flip( );
    //    long bytesWritten = channel.write (buffers);

    }
}
