import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.StreamSupport;

public class ServerManager {

    private ExecutorService executor;
    private Server server;

    public ServerManager(int threads) {
        server = new Server(threads);
        raiseServers();
    }

    private void raiseServers() {
        executor = Executors.newFixedThreadPool(1);
        executor.execute(new Thread(() -> server.start()));
    }

    public void stopServers() {
        while (server.isWorking()){}
        System.out.println("Server finished the task, want to shut it down?(yes/no)");
        Scanner input = new Scanner(System.in);
        String ans = "";
        while (!ans.equals("yes")) {
            ans = input.next();
            if (ans.equals("yes")) {
                server.setStop(true);
                executor.shutdown();
            }
        }
    }
}
