import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.communication.StatusCode;
import static com.hacklympics.api.proctor.Snapshot.create;

public class ImageTest {
    
    public static void main(String[] args) throws IOException {
        // Read an image into a byte array, encode it as base64 string
        // and send it to the django server.
        File f = new File("./screenshot_thumbnail.jpg");
	byte[] byteArray = Files.readAllBytes(f.toPath());
        
	String imageString = Base64.getEncoder().encodeToString(byteArray);
        
        Response snapshot = create(7, 10, "1080630202", imageString);
        if (snapshot.getStatusCode() == StatusCode.SUCCESS) {
            System.out.println("Damn Train");
        }
    }
    
}
