import com.hacklympics.api.communication.Response;
import com.hacklympics.api.materials.Course;
import com.hacklympics.api.materials.Exam;

public class ExamTest {
    
    
    public static void main(String[] args) {
        Course systemSoftwares = new Course(1);
        
        /*
        Response response = Exam.create(
                "Compiler",
                "Implement a simple compiler.",
                180,
                systemSoftwares.getCourseID()
        );
        
        System.out.println(response);
        */
        
        
        Response response = systemSoftwares.remove();
        System.out.println(response);
    }
    
}
