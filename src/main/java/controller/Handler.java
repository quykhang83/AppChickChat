package controller;

import model.Account;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

// Luồng riêng dùng để giao tiếp với mỗi user
// Object để synchronize các hàm cần thiết
// Các client đều có chung object này được thừa hưởng từ chính server
public class Handler implements Runnable {

    private final Object lock;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private Account account;

    public Handler(Socket socket1, Account account1, Object lock1) throws IOException {
        this.socket = socket1;
        this.account = account1;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.lock = lock1;
    }

    public String getUsername() {
        return this.account.getUserName();
    }

    public String getPassword() {
        return this.account.getPassword();
    }

    public DataOutputStream getOutput() {
        return this.output;
    }

    public String getAvatr() {
        return account.getAvatar();
    }

    public void setPassword(String pass) {
        account.setPassword(pass);
    }

    public void setAvatar(String Avatar) {
        account.setAvatar(Avatar);
    }

    @Override
    public void run() {

        while (true) {
            try {
                // Đọc yêu cầu từ user
                String[] messageReceived = input.readUTF().split(",");

                // Yêu cầu đăng xuất từ user
                if (messageReceived[0].equals("Log out")) {

                    // Đóng socket và chuyển trạng thái thành offline
                    Server.LogOutAccount(this);
                    socket.close();
                    // Thông báo cho các user khác cập nhật danh sách người dùng trực tuyến
                    Server.updateOnlineUsers();
                    break;

                } // Yêu cầu gửi tin nhắn dạng văn bản
                else if (messageReceived[0].equals("Text")) {
                    String sender = messageReceived[1];
                    String receiver = messageReceived[2];
                    String content = messageReceived[3];
                    
                    for (Handler client : Server.clients) {
                        if (client.getUsername().equals(receiver)) {
                            synchronized (lock) {
                                String messageSent = "Text" + "," + sender + "," + 
                                        receiver + "," + content;
                                client.getOutput().writeUTF(messageSent);
                                client.getOutput().flush();
                                break;
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}