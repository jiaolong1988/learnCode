package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


/**
 * https://www.codejava.net/java-se/networking/java-socket-server-examples-tcp-ip
 */
public class TcpServer {

    /**
     * Socket服务端
     */
    public static void main(String[] args) {
        int port = Integer.parseInt("8889");

        String result = "<?xml version=\"1.0\" standalone=\"yes\"?>\n" +
                "<Device Visitor=\"210101001\" Target=\"210100000\" Action=\"1\" Time=\"2021-01-19 14:36:40\" Index=\"1\" Licence=\"\">\n" +
                "  <Command Code=\"01\" Er=\"1\" Es=\"登录失败,用户不存在\"/>\n" +
                "</Device>";

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println(serverSocket.getLocalPort());

            System.out.println("Server is listening on port " + port);
            int i=0;
            while (true) {
                System.out.println("------new---------");

                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                //接收数据
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input,"GB2312"));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println("收到客户端信息: "+line);
                }


                //发送数据
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(),"GB2312");
                PrintWriter writer = new PrintWriter(osw, true);
                writer.println(result);


                System.out.println("=====server====== "+ (i+=1));

                //发送数据的结束标志
                //socket.shutdownOutput();

                    System.out.println("睡眠开始");
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("睡眠结束");

                System.out.println("程序执行结束。。。");

                socket.close();
                System.out.println("------end---------");
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}