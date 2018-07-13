import com.hacklympics.api.communication.Response;
import com.hacklympics.api.communication.StatusCode;
import static com.hacklympics.api.proctor.Snapshot.create;
import hacklympics.utility.Utils;
import java.io.File;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;

public class SnapshotTest {
    
    public static byte[] bufferedImage2ByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
    
    public static BufferedImage byteArray2BufferedImage(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return ImageIO.read(bais);
    }

    public static void main(String[] args) throws AWTException, IOException {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        BufferedImage thumbnail = Thumbnails.of(image).scale(0.25).asBufferedImage();
        
        /*
        ImageIO.write(image, "png", new File("screenshot.png"));
        
        Thumbnails.of(new File("screenshot.png"))
                .size(640, 480)
                .toFile("screenshot_thumbnail.jpg");
        */
        
        String imageString = Base64.getEncoder().encodeToString(Utils.bufferedImage2ByteArray(thumbnail));
        
        Response snapshot = create(7, 10, "1080630202", imageString);
        if (snapshot.getStatusCode() == StatusCode.SUCCESS) {
            System.out.println("Damn Train");
        }
    }
    
}