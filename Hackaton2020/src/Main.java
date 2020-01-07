import java.net.SocketException;
import java.net.UnknownHostException;



public class Main {

    public static void main(String[] args) throws UnknownHostException, SocketException, InterruptedException {

        Client client = new Client();
        client.requestHash();
        ServerManager sv = new ServerManager(1);
        client.sendDiscover();
        String ans = client.sendRequestToServers();
        System.out.println(ans);
        sv.stopServers();
        //System.out.println(client.getHintForTeamName());
    }
}
