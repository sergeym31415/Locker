import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Screenshot {
    public static void main(String[] args) {
        getScreenshot();
    }

    public static File getScreenshot() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
        String fileName = "screenshots/screen_" + timeStamp + ".png";
        File directory = new File("screenshots");
        directory.mkdirs();
        File file = new File(fileName);
        try {
            Rectangle screenRect = new Rectangle(0, 0, 0, 0);
            for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                screenRect = screenRect.union(gd.getDefaultConfiguration().getBounds());
            }
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            ImageIO.write(capture, "png", file);
            return file;
        } catch (AWTException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
