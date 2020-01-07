import java.net.SocketException;
import java.net.UnknownHostException;



public class Main {

    public static void main(String[] args) {

        Client client = new Client();
        client.requestHash();
        ServerManager sv = new ServerManager(4);
        client.sendDiscover();
        String ans = client.sendRequestToServers();
        System.out.println(ans);
        sv.stopServers();
        //System.out.println(client.getHintForTeamName());
    }
}
