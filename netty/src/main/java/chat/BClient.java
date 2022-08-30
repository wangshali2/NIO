package chat;

/**
 * @Author wsl
 * @Description
 */
public class BClient {
    public static void main(String[] args) throws Exception {
        new GroupChatClient("127.0.0.1", 7000).run();
    }
}
