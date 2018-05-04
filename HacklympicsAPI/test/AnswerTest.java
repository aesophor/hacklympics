import com.hacklympics.api.communication.Response;
import com.hacklympics.api.materials.Course;
import com.hacklympics.api.materials.Exam;
import com.hacklympics.api.materials.Problem;
import com.hacklympics.api.materials.Answer;

public class AnswerTest {
    
    public static void main(String[] args) {
        Course systemSoftwares = new Course(7);
        Exam e = new Exam(systemSoftwares.getData().getCourseID(), 10);
        Problem p = new Problem(e.getData().getCourseID(),
                                e.getData().getExamID(),
                                7);
        
        /*
        Answer a = new Answer(p.getData().getCourseID(),
                              p.getData().getExamID(),
                              p.getData().getProblemID(),
                              19);
        */
        
        /*
        Response response = Answer.list(p.getData().getCourseID(),
                                        p.getData().getExamID(),
                                        p.getData().getProblemID());
        */
        
        
        Response response = Answer.create(
                p.getCourseID(),
                p.getExamID(),
                p.getProblemID(),
                "Test.java",
                "public class Test { public static void main(String[] args) {} }",
                "1080630212"
        );
        
        System.out.println(response);
        
        
        //Response response = a.update("Test.java", "import sys");
        
        // Response response = a.remove();
        // System.out.println(response);
        
        
        //Response response = a.validate();
        //System.out.println(response);
    }
    
}
