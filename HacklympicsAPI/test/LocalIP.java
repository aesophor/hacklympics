import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class LocalIP {
    
    public static void main(String[] args) throws SocketException {
        List<String> localAddresses = new LinkedList();
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                localAddresses.add(i.getHostAddress());
            }
        }
        
        String localAddress = getLocalipv4(localAddresses);
        System.out.println(localAddress);
    }
    
    private static String getLocalipv4(List<String> addresses) {
        for (String address : addresses) {
            if (address.startsWith("192.168") | address.startsWith("172.16")) {
                return address;
            }
        }
        return null;
    }
    
}
