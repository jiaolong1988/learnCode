package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.io.*;
import java.nio.charset.Charset;


/**
 * https://www.codejava.net/java-se/networking/java-socket-client-examples-tcp-ip
 */
public class TcpClient {
    public static final String ETX = "\u0003";
    public static void main(String[] args) {

        String hostname;
        int port = 0 ;

        String flag = "test1";
        if(flag.equals("test")){
             hostname = "127.0.0.1";
             port = Integer.parseInt("8889");
        }else{
            hostname = "122.114.22.173";
            port = Integer.parseInt("9200");
        }


        String info = "<?xml version=\"1.0\" encoding=\"GB2312\"?>\n" +
                "<Device Visitor=\"210102001\" Target=\"210101088\" Action=\"0\" Time=\"2021-01-19 16:09:44\" Index=\"1\" Licence=\"\">\n" +
                "  <Command Code=\"01\">\n" +
                "    <Login Name=\"admin\" Password=\"123456\"/>\n" +
                "  </Command>\n" +
                "</Device>\n  ";

        try (Socket socket = new Socket(hostname, port)) {

            //客户端发送数据
            OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream(),"GB2312");
            PrintWriter writer = new PrintWriter(output, true);//设置自动刷新

            writer.println(info);
            writer.println(ETX);
            //设置数据结束标志
          //  socket.shutdownOutput();

            System.out.println("----------------");
            //客户端接收数据
            InputStreamReader input = new InputStreamReader(socket.getInputStream(),"GB2312");
            BufferedReader reader = new BufferedReader(input);


            BufferedInputStream inScanner = new BufferedInputStream(socket.getInputStream());
            byte[] buf = new byte[1024];
            int len;
            String resp = "";
            while ((len = inScanner.read(buf)) != -1) {
                resp = new String(buf, 0, len, Charset.forName("GB2312"));
                System.out.println(resp);
                byte endBytes = buf[len - 1];
//                if (endBytes == 3) {
//                    break;
//                }
            }

//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                if(line.equals(ETX))
//                    System.out.println("收到结束标志"+line);
//                else
//                    System.out.println("收到服务器信息: "+line);
//            }



            System.out.println("睡眠开始");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("睡眠结束");

            System.out.println("程序执行结束。。。");

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }

    }
}


