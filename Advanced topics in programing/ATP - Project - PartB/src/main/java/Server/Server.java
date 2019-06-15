
package Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Aviadjo on 3/2/2017.
 */
public class Server {
    private int port;
    private int listeningInterval;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    private ExecutorService executor ;
    private static final Logger LOG = LogManager.getLogger(); //Log4j2


    public Server(int port, int listeningInterval, IServerStrategy serverStrategy) {
        this.port = port;
        this.listeningInterval = listeningInterval;
        this.serverStrategy = serverStrategy;
        //this.executor = Executors.newFixedThreadPool(getPoolSize());

    }

    public void start() {
        new Thread(() -> {
            runServer();
        }).start();
    }

    private void runServer() {
        try {
            executor = Executors.newFixedThreadPool(getPoolSize());
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningInterval);
            LOG.info(String.format("Server starter at %s!", serverSocket));
            LOG.info(String.format("Server's Strategy: %s", serverStrategy.getClass().getSimpleName()));
            LOG.info("Server is waiting for clients...");
            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept(); // blocking call
                    LOG.info(String.format("Client excepted: %s", clientSocket));
                    executor.execute(new Thread(() -> {
                        handleClient(clientSocket);
                    }));
                    LOG.info(String.format("Finished handle client: %s", clientSocket));
                } catch (SocketTimeoutException e) {
                    LOG.debug("Socket Timeout - No clients pending!");
                }
            }
            serverSocket.close();
            executor.shutdown();
        } catch (IOException e) {
            LOG.error("IOException", e);
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            LOG.info(String.format("Handling client with socket: %s", clientSocket.toString()));
            serverStrategy.serverStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            clientSocket.close();
        } catch (IOException e) {
            LOG.error("IOException", e);
        }
    }

    public void stop() {

        LOG.info("Stopping server..");
        stop = true;
    }

    private int getPoolSize() {
        return Integer.parseInt(Configurations.getThreadPoolAmount());
    }
}
