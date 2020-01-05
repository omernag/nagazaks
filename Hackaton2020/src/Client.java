
import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;

public class Client {
    private InetAddress address;
    private DatagramSocket socket;

    private byte[] buf;
    private final String teamName = "abcdeabcdeabcdeabcdeabcdeabcdeab";
    private LinkedList<InetAddress> readyServers;
    private final int ttl = 15000;
    private final int UDP_PORT = 3117;

    private String hashS;
    private int size;

    public Client() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public String getReadyServers() {
        return readyServers.toString();
    }

    public void requestHash() {
        System.out.println("Welcome to <your-team-name-here>. Please enter the hash:");
        while (true) {
            Scanner input = new Scanner(System.in);
            //hashS = input.next();
            hashS = "9017347a610d1436c1aaf52764e6578e8fc1a083";
            if (hashS.length() == 40) {
                break;
            } else {
                System.out.println("Not a valid hash, Try again");
            }
        }
        System.out.println("Please enter the input string length:");
        while (true) {
            Scanner input = new Scanner(System.in);
            //size = input.nextInt();
            size = 5;
            if (size > 0 && size < 10) {
                break;
            } else {
                System.out.println("Not a valid length, Try again");
            }
        }
        readyServers = new LinkedList<>();
        sendDiscover();
    }

    private void sendDiscover() {
        try {
            String dis = new Message(teamName, Type.DISCOVER, "", '1', "", "").toSend();
            buf = dis.getBytes();

            socket.setBroadcast(true);
            InetAddress ip = InetAddress.getByName("255.255.255.255");

            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, UDP_PORT);
            socket.send(packet);

            packet = new DatagramPacket(buf, buf.length);

            socket.setSoTimeout(ttl/10);
            while (!socket.isClosed()) {
                socket.receive(packet);
                if (packet.getData()[32] == 50) { //change 50 to 2 some how
                    readyServers.add(packet.getAddress());
                }
            }
        } catch (IOException e) {
            if(readyServers.size()==0) {
                System.out.println("Socket timed out, want to try again?(y/n)");
                Scanner input = new Scanner(System.in);
                String answer = input.next();
                if (answer.equals("y")) {
                    sendDiscover();
                }
            }
        }
    }

    public String sendRequestToServers() {
        HelperFunctions hp = new HelperFunctions();
        try {
            socket = new DatagramSocket();
            String[] divides = hp.divideToDomains(size, readyServers.size());
            for (InetAddress ip : readyServers
            ) {
                buf = new Message(teamName, Type.REQUEST, hashS, (char) size, divides[0], divides[1]).toSend().getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, UDP_PORT);
                socket.send(packet);
            }
            int counter = 0;
            socket.setSoTimeout(ttl);
            while (counter < readyServers.size()) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                if (packet.getData()[32] == 52) { //change 50 to 2 some how
                    socket.close();
                    return new String(packet.getData()).substring(74, 74 + size);
                } else if (packet.getData()[32] == 53) {
                    counter++;
                }
            }
            socket.close();
            return null;
        } catch (IOException e) {
            System.out.println("Socket timed out, want to try again?(y/n)");
            Scanner input = new Scanner(System.in);
            String answer = input.next();
            if (answer.equals("y")) {
                sendRequestToServers();
            }
            socket.close();
            return null;
        }
    }
}
