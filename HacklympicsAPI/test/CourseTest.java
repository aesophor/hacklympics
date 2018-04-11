import java.util.List;
import java.util.ArrayList;
import com.hacklympics.api.material.Course;
import static com.hacklympics.api.material.Course.create;

public class CourseTest {

    public static void main(String[] args) {
        Course systemSoftwares = new Course(1);
        System.out.println(systemSoftwares);
        
        List<String> students = new ArrayList<>();
        students.add("andrey");
        students.add("tim"); 
        System.out.println(create("Operating System Concpets", 1062, "max", students));
    }
    
}
