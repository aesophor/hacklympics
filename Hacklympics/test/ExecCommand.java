import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecCommand {
    
    public static void main(String[] args) throws IOException {
        String line;
        Process p = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", "cat ~/.zsh_history | grep shell"});

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        in.close();
    }
    
}
