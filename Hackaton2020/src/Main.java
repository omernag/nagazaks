import java.net.SocketException;
import java.net.UnknownHostException;



public class Main {

    public static void main(String[] args) throws UnknownHostException, SocketException {
        ServerManager sv = new ServerManager(1);
        Client client = new Client();
        client.requestHash();
        System.out.println(client.getReadyServers());
        String ans = client.sendRequestToServers();
        System.out.println(ans);
        sv.stopServers();
    }
}
