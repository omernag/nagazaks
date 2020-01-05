import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerManager {

    private ExecutorService executor;
    private int size;
    private Server[] servers;

    public ServerManager(int size) throws UnknownHostException, SocketException {
        this.size = size;
        servers = new Server[size];
        for (int i=0; i<size;i++){
            servers[i]=new Server(1, "8.8.8.");
        }
        raiseServers();
    }

    private void raiseServers(){
        executor = Executors.newFixedThreadPool(size);
        for (int i=0; i <size; i++) {
            int index=i;
            executor.execute(new Thread(() ->  servers[index].start()));
        }

    }

    public void stopServers(){
        for (Server sv: servers
             ) {
            sv.setStop(true);
        }
        executor.shutdown();
        System.out.println();
    }

   /* public String serversStatus(){
        String ans ="";
        for (Server sv: servers
             ) {
            ans+=sv.printStatus();
        }
        return ans;
    }*/
}
