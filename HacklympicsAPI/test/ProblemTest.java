import com.hacklympics.api.communication.Response;
import com.hacklympics.api.materials.Course;
import com.hacklympics.api.materials.Exam;
import com.hacklympics.api.materials.Problem;

public class ProblemTest {
    
    public static void main(String[] args) {
        Course systemSoftwares = new Course(7);
        Exam e = new Exam(systemSoftwares.getData().getCourseID(), 4);
        Problem p = new Problem(e.getData().getCourseID(),
                                e.getData().getExamID(),
                                4);
        
        /*
        Response response = Problem.create(
                "Test Problem Title",
                "Test problem desc.",
                systemSoftwares.getCourseID(),
                firstPass.getExamID()
        );
        
        System.out.println(response);
        */
        
        /*
        Response response = Problem.remove(systemSoftwares.getData().getCourseID(),
                                           firstPass.getData().getExamID(),
                                           1);
        System.out.println(response);
        */
        
        Response response = p.update("First pass", "lll?", "", "");
        System.out.println(response);
    }
    
}
