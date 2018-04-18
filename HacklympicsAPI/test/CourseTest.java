import java.util.List;
import java.util.ArrayList;
import com.hacklympics.api.materials.Course;
import static com.hacklympics.api.materials.Course.create;
import java.util.HashMap;
import java.util.Map;

public class CourseTest {

    public static void main(String[] args) {
        Course systemSoftwares = new Course(1);
        //System.out.println(systemSoftwares);
        
        //List<String> students = new ArrayList<>();
        //students.add("andrey");
        //students.add("tim"); 
        //System.out.println(Course.create("Operating System Concpets", 1062, "max", students));
        
        //System.out.println(Course.remove(5));
        
        System.out.println(systemSoftwares.getData().getStudents());
    }
    
}
