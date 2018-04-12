import com.hacklympics.api.communication.Response;
import com.hacklympics.api.material.Course;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.material.Problem;

public class ProblemTest {
    
    
    public static void main(String[] args) {
        Course systemSoftwares = new Course(1);
        Exam firstPass = new Exam(systemSoftwares.getCourseID(), 1);
        
        /*
        Response response = Problem.create(
                "Test Problem Title",
                "Test problem desc.",
                systemSoftwares.getCourseID(),
                firstPass.getExamID()
        );
        
        System.out.println(response);
        */
        
        
        Response response = Problem.remove(systemSoftwares.getCourseID(),
                                           firstPass.getExamID(),
                                           1);
        System.out.println(response);
        
    }
    
}
