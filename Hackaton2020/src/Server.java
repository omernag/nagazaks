import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private volatile boolean stop;
    private ExecutorService executor ;
    private int pool_size;
    private byte[] buf = new byte[256];
    private String status;
    private String teamName = "abcdeabcdeabcdeabcdeabcdeabcdeab";


    public Server(int poolS) {
        this.pool_size=poolS;
        //start();
        status = "on";
    }

    public void start() {
        new Thread(() -> runServer()).start();
    }

    private void runServer() {
        try {
            //executor = Executors.newFixedThreadPool(pool_size);
            DatagramSocket serverSocket = new DatagramSocket(3117);
            while (!stop) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    serverSocket.receive(packet);
                    handlePacket(serverSocket,packet);
                    //executor.execute(new Thread(() -> handlePacket(serverSocket,packet)));
                } catch (SocketTimeoutException e) {
                }
            }
            serverSocket.close();
            //executor.shutdown();
        } catch (IOException e) {
        }
    }

    private void handlePacket(DatagramSocket serverSocket,DatagramPacket packet) {
        try {
            //write the handle function
            //serverStrategy.serverStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            if (packet.getData()[32]==49) {//change back some how
                buf=new Message(teamName,Type.OFFER,"",'1',"","").toSend().getBytes();
            }
            packet = new DatagramPacket(buf, buf.length, address, port);
            serverSocket.send(packet);

        } catch (Exception e) {
        }
    }

    public String printStatus(){
        return status+"\n";
    }

    public void stop() {
        stop = true;
    }

}
