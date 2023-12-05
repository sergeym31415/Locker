import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final static int SERVER_PORT = 9876;
    private final static String COMMAND_LOCK = "lock";
    private final static String COMMAND_SLEEP = "sleep";
    private final static String COMMAND_SCREEN = "screen";


    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Server: socket accepted");
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                OutputStream out = socket.getOutputStream();

                String message = "";
                try {
                    message = (String) objectInputStream.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Message recieved: " + message + " From: " + socket.getInetAddress() + "\n");
                if (message.equalsIgnoreCase(COMMAND_LOCK)) {
                    try {
                        Runtime.getRuntime()
                                .exec("C:\\Windows\\System32\\rundll32.exe user32.dll,LockWorkStation");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (message.equalsIgnoreCase(COMMAND_SLEEP)) {
                    try {
                        Runtime.getRuntime()
                                .exec("C:\\Windows\\System32\\rundll32.exe powrprof.dll,SetSuspendState Sleep");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (message.equalsIgnoreCase(COMMAND_SCREEN)) {
                    File file = Screenshot.getScreenshot();
                    byte[] bytes = new byte[16 * 1024];
                    try (InputStream in = new FileInputStream(file)) {
                        int count;
                        while ((count = in.read(bytes)) > 0) {
                            out.write(bytes, 0, count);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                out.write(("Server received: " + message).getBytes());
                out.flush();
                objectInputStream.close();
                out.close();
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Server crashed");
            throw new RuntimeException(e);
        }
    }
}
