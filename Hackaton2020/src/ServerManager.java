import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerManager {

    private ExecutorService executor;
    private int size;
    private Server[] servers;

    public ServerManager(int size) {
        this.size = size;
        servers = new Server[size];
        for (int i=0; i<size;i++){
            servers[i]=new Server(1);
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
        executor.shutdown();
    }

    public String serversStatus(){
        String ans ="";
        for (Server sv: servers
             ) {
            ans+=sv.printStatus();
        }
        return ans;
    }
}
