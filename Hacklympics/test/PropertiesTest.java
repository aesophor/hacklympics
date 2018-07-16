import java.util.*;
import java.io.*;

public class PropertiesTest {

    public static void main(String[] args) throws Exception {

        Properties p = new Properties();
        p.setProperty("server.hostname", "192.168.19.4");
        p.setProperty("server.port", "8000");
        p.setProperty("server.port", "8001");

        p.store(new FileWriter("config.properties"), "Hacklympics client properties");

    }
}
