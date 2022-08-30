import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author wsl
 * @Description  Stream 是单向
 * 一个客户端启动一个线程，连接数量超出会导致服务器崩溃
 */
public class TcpDemo {
    public static void main(String[] args) throws IOException {

        Thread thread = new Thread(() -> {

            try {
                ServerDemo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(false);
        thread.start();


        ClientDemo();

    }

    public static void ServerDemo() throws IOException {
        //指定port
        ServerSocket ss = new ServerSocket(9999);

        //监听
        InputStream reader = ss.accept().getInputStream();

        byte[] b = new byte[1024];
        int len;
        //读取数据
        while ((len = reader.read(b)) != -1) {
            System.out.println(new String(b, 0, len));
        }

       reader.close();
       ss.close();
    }

    public static void ClientDemo() throws IOException {
        //指定port ip
        Socket socket = new Socket("127.0.0.1", 9999);

        OutputStream write = socket.getOutputStream();
        //写数据
        write.write("我的世界".getBytes());

        write.close();
        socket.close();
    }
}
