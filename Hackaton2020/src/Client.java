import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;

public class Client {
    private InetAddress address;
    private DatagramSocket socket;
    private byte[] buf;
    private String teamName = "abcdeabcdeabcdeabcdeabcdeabcdeab";
    private LinkedList<InetAddress> readyServers;

    public Client() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void communicateWithServer() {
        try {
            sendEcho("whywhywhy");
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendEcho(String msg) {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 4445);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }

    public String getReadyServers() {
        return readyServers.toString();
    }

    public void requestHash() {
        /*System.out.println("Welcome to <your-team-name-here>. Please enter the hash:");
        String hash;
        while(true) {
            Scanner input = new Scanner(System.in);
            hash = input.next();
            if(hash.length()==40){
                break;
            }
            else{
                System.out.println("Not a valid hash, Try again");
            }
        }
        System.out.println("Please enter the input string length:");
        int size;
        while (true) {
            Scanner input = new Scanner(System.in);
            size = input.nextInt();
            if(size>0 && size<10){
                break;
            }
            else{
                System.out.println("Not a valid length, Try again");
            }
        }*/
        readyServers = new LinkedList<InetAddress>();
        try {
            sendDiscover();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendDiscover() throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        String dis = new Message(teamName, Type.DISCOVER, "", '1', "", "").toSend();
        buf = dis.getBytes();
        InetAddress ip = InetAddress.getByName("255.255.255.255");

        DatagramPacket packet = new DatagramPacket(buf, buf.length, ip,3117 );
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        try {
            socket.setSoTimeout(15000);
            while (!socket.isClosed()) {
                socket.receive(packet);
                if (packet.getData()[32] == 50) { //change 50 to 2 some how
                    readyServers.add(packet.getAddress());
                }
            }
        } catch (IOException e) {
        }
    }
}
