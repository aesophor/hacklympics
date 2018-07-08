import java.io.File;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;

public class SnapshotTest {

    public static void main(String[] args) throws AWTException, IOException {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(image, "png", new File("screenshot.png"));
        
        Thumbnails.of(new File("screenshot.png"))
                .size(640, 480)
                .toFile("screenshot_thumbnail.jpg");
    }
    
}