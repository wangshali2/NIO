import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @Author wsl
 * @Description
 */
public class UdpDemo {
    public static void main(String[] args) throws IOException {
        Thread thread = new Thread(() -> {

            try {
                ReceiveDemo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(false);
        thread.start();


        SendDemo();
    }

    public static void ReceiveDemo() throws IOException {
        DatagramSocket ds = new DatagramSocket(9999);

        byte[] bytes = new byte[1024];
        int len = bytes.length;
        DatagramPacket dp = new DatagramPacket(bytes, len);

        ds.receive(dp);

        //获取ip
        String ip = dp.getAddress().getHostAddress();
        //获取数据包
        byte[] data = dp.getData();
        System.out.println(ip + ":" + new String(data, 0, dp.getLength()));

        ds.close();
    }

    public static void SendDemo() throws IOException {
        DatagramSocket ds = new DatagramSocket();

        byte[] bytes = "最美的不是下雨天".getBytes();

        InetAddress ip = InetAddress.getByName("127.0.0.1");
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, ip, 9999);
        ds.send(dp);

        ds.close();

        InetAddress host = InetAddress.getLocalHost();
        System.out.println(host.getHostName());  //LAPTOP-2L6J4IS5
        System.out.println(host.getHostAddress());  //192.168.31.123

    }
}
