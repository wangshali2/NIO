import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author wsl
 * @Description 优化：用连接池，telnet 127.0.0.1 6666
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {


        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了...");
        while (true){
            System.out.println("线程信息id = " + Thread.currentThread().getId() + "名字 = " + Thread.currentThread().getName());

            final Socket socket = serverSocket.accept();

                newCachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        //可以和客户端通讯
                        handler(socket);
                    }
                });
        }
    }

    public static void handler(Socket socket) {
        try {
            System.out.println("正在与线程信息id = " + Thread.currentThread().getId() + "名字 = " + Thread.currentThread().getName()+ "通信...");
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();


            while (true) {

                int readLen = inputStream.read(bytes);
                if (readLen != -1) {
                    System.out.println("线程" + Thread.currentThread().getId() + ":" +new String(bytes, 0, readLen));//输出客户端发送的数据
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
