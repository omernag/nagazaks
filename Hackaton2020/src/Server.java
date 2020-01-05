import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ExecutorService executor ;
    private int pool_size;
    private volatile boolean stop=false;

    private byte[] buf = new byte[256];
    private final String teamName = "abcdeabcdeabcdeabcdeabcdeabcdeab";
    private final int ttl=10000;

    private final int UDP_PORT=3117;
    DatagramSocket serverSocket;
    private InetAddress address;



    public Server(int poolS, String byName) throws UnknownHostException, SocketException {
        this.pool_size=poolS;
        //address=InetAddress.getByName(byName+(int)(Math.random()*10));
        //serverSocket=new DatagramSocket();
    }



    public void start() {
        runServer();
    }

    private void runServer() {
        try {
            serverSocket=new DatagramSocket(UDP_PORT);
            serverSocket.setSoTimeout(ttl);
            while (!serverSocket.isClosed() && !stop) {
                try {
                    buf=new byte[256];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    serverSocket.receive(packet);
                    handlePacket(serverSocket,packet);
                } catch (SocketTimeoutException e) {
                    if(!stop) {
                        System.out.println("Socket timed out, want to try again?(y/n)");
                        Scanner input = new Scanner(System.in);
                        String answer = input.next();
                        if (answer.equals("y")) {
                            serverSocket.setSoTimeout(ttl);
                        }
                    }
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePacket(DatagramSocket serverSocket,DatagramPacket packet) {
        HelperFunctions hf = new HelperFunctions();
        try {
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            if (packet.getData()[32]==49) {//change back some how
                buf=new Message(teamName,Type.OFFER,"",'1',"","").toSend().getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port);
                serverSocket.send(packet);
            }
            else if(packet.getData()[32]==51){
                System.out.println("request came");
                int index = (packet.getLength()-32-1-40-1)/2;//74
                String stream = new String(packet.getData());
                String start = stream.substring(74,74+index);
                String end = stream.substring(74+index,74+2*index);
                String hash=stream.substring(33,73);
                System.out.println(start+"\n"+end+"\n"+hash);
                String check = hf.tryDeHash(start,end,hash);
                if(check!=null){
                    buf=new Message(teamName,Type.ACK,hash,'0',check,"").toSend().getBytes();
                }
                else{
                    buf=new Message(teamName,Type.NACK,"",'1',"","").toSend().getBytes();
                }
                packet = new DatagramPacket(buf, buf.length, address, port);
                serverSocket.send(packet);
            }


        } catch (Exception e) {
            System.out.println("handelpacket");
        }
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
