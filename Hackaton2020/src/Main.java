import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        ServerManager sv = new ServerManager(2);
        //System.out.println(sv.serversStatus());
        Client client = new Client();
        client.requestHash();
        System.out.println(client.getReadyServers());
        sv.stopServers();

    }
}
