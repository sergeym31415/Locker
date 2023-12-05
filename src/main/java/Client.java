import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Client {
    private final static int SERVER_PORT = 9876;
    private final static String COMMAND_SCREEN = "screen";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter command: [LOCK, SLEEP, SCREEN]");
        String command = scanner.nextLine();
        System.out.println("Enter Server's ip address: like 127.0.0.1");
        String ipAddress = scanner.nextLine();
        startClient(command, ipAddress);
    }

    private static void startClient(String command, String ipAddress) {
        try (Socket socket = new Socket(InetAddress.getByName(ipAddress), SERVER_PORT)) {
            System.out.println("Client started");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(command);
            System.out.println("Client sent message: " + command);
            InputStream in = socket.getInputStream();
            if (command.equalsIgnoreCase(COMMAND_SCREEN)) {
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                        .format(Calendar.getInstance().getTime());
                String fileName = "screenshots/cl_screen_" + timeStamp + ".png";
                File file = new File(fileName);
                File directory = new File("screenshots");
                directory.mkdirs();
                file.createNewFile();
                OutputStream out = new FileOutputStream(file);
                byte[] bytes = new byte[16 * 1024];
                int count;
                while ((count = in.read(bytes)) > 0) {
                    out.write(bytes, 0, count);
                }
                out.close();
            }
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                String output = new String(buffer, 0, read);
                System.out.println("Message from Server: " + output);
            }
            in.close();
            objectOutputStream.close();
            System.out.println("Client ended");
        } catch (Exception e) {
            System.out.println("Client crashed");
            e.printStackTrace();
        }
    }
}
