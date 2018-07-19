import com.hacklympics.api.communication.Response;
import com.hacklympics.api.material.Course;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.material.Problem;
import com.hacklympics.api.material.Answer;

public class AnswerTest {
    
    public static void main(String[] args) {
        Course systemSoftwares = new Course(7);
        Exam e = new Exam(systemSoftwares.getCourseID(), 10);
        Problem p = new Problem(e.getCourseID(), e.getExamID(), 7);
        
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
