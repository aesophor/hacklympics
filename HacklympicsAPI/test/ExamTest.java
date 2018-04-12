import com.hacklympics.api.communication.Response;
import com.hacklympics.api.material.Course;
import com.hacklympics.api.material.Exam;

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
        
        
        Response response = Exam.remove(systemSoftwares.getCourseID(), 3);
        System.out.println(response);
    }
    
}
