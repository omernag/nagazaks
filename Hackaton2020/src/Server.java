import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private volatile boolean stop=false;
    private ExecutorService exe;
    private byte[] buf = new byte[256];
    private final String teamName = "bW9zdC1jeWJlcmktbmFtZS1hcm91bmQ=";
    private final int ttl=500000;
    private volatile boolean working=false;

    private final int UDP_PORT=3117;
    private DatagramSocket serverSocket;

    public Server(int poolS)  {
        exe = Executors.newFixedThreadPool(poolS);
    }

    public void start()   {
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
                    exe.execute(new Thread(() -> handlePacket(serverSocket,packet)));
                    working=true;
                } catch (SocketTimeoutException e) {
                    if(!stop && !working) {
                        System.out.println("Server Socket timed out, want to try again?(y/n)");
                        Scanner input = new Scanner(System.in);
                        String answer = input.next();
                        if (answer.equals("y")) {
                            serverSocket.setSoTimeout(ttl*2);
                        }
                    }
                }
            }
            serverSocket.close();
            exe.shutdown();
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    private void handlePacket(DatagramSocket serverSocket,DatagramPacket packet) {
        HelperFunctions hf = new HelperFunctions();
        try {
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            if (packet.getData()[32]==49 || packet.getData()[32]==1) {
                buf=new Message(teamName,Type.OFFER,"",'0',"","").toSend().getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port);
                serverSocket.send(packet);
                System.out.println("OFFER has sent to " + address);
            }
            else if(packet.getData()[32]==51 || packet.getData()[32]==3){
                System.out.println("REQUEST came from "+address);
                //int index = (packet.getLength()-32-1-40-1)/2;//74

                String stream = new String(packet.getData());
                int index;
                try{
                     index = Integer.parseInt(stream.substring(73,74));
                }
                catch (Exception e){
                     index = stream.charAt(73);
                }
                String start = stream.substring(74,74+index);
                String end = stream.substring(74+index,74+2*index);
                String hash=stream.substring(33,73);
                System.out.println("Start from: "+start+"\n"+"End at: "+end+"\n"+"The original hash: "+hash);
                String check = hf.tryDeHash(start,end,hash);
                if(check!=null){
                    buf=new Message(teamName,Type.ACK,hash,(char)packet.getData()[73],check,"").toSend().getBytes();
                }
                else{
                    buf=new Message(teamName,Type.NACK,hash,(char)packet.getData()[73],start,end).toSend().getBytes();
                    System.out.println(address+ " Didn't found ");
                }
                packet = new DatagramPacket(buf, buf.length, address, port);
                serverSocket.send(packet);
            }
            working=false;

        } catch (Exception e) {
            System.out.println("Received packet in a wrong format - probably malicious");
        }
    }

    public boolean isWorking() {
        return working;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
