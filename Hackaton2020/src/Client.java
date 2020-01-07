
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;

public class Client {
    private DatagramSocket socket;
    private byte[] buf;
    private final String teamName = "bW9zdC1jeWJlcmktbmFtZS1hcm91bmQ=";
    private LinkedList<InetAddress> readyServers;
    private final int ttl = 15000;
    private final int UDP_PORT = 3117;

    private String hashS;
    private int size;

    public Client() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public String getReadyServers() {
        return readyServers.toString();
    }

    public void requestHash() {
        System.out.println("Welcome to "+teamName+". Please enter the hash:");
        while (true) {
            Scanner input = new Scanner(System.in);
            hashS = input.next();
            //hashS = "a346f3083515cbc8ca18aae24f331dee2d23454b";
            if (hashS.length() == 40) {
                break;
            } else {
                System.out.println("Not a valid hash, Try again");
            }
        }
        System.out.println("Please enter the input string length:");
        while (true) {
            Scanner input = new Scanner(System.in);
            size = input.nextInt();
            //size = 5;
            if (size > 0 && size < 10) {
                break;
            } else {
                System.out.println("Not a valid length, Try again");
            }
        }
        readyServers = new LinkedList<>();
        //sendDiscover();
    }

    public void sendDiscover() {
        try {
            String dis = new Message(teamName, Type.DISCOVER, hashS, (char)size, "aaaaa", "zzzzz").toSend();
            buf = dis.getBytes();

            socket.setBroadcast(true);
            InetAddress ip = getBroadCastIP();

            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, UDP_PORT);
            socket.send(packet);

            packet = new DatagramPacket(buf, buf.length);

            socket.setSoTimeout(ttl/5);
            while (!socket.isClosed()) {
                socket.receive(packet);
                if (packet.getData()[32] == 50) {
                    readyServers.add(packet.getAddress());
                }
            }
        } catch (SocketTimeoutException e) {
            if(readyServers.size()==0) {
                System.out.println("Client OFFER Socket timed out, want to try again?(y/n)");
                Scanner input = new Scanner(System.in);
                String answer = input.next();
                if (answer.equals("y")) {
                    sendDiscover();
                }
            }
        }
        catch (IOException e){
            System.out.println("cant receive message"+socket.toString());
            sendDiscover();
        }
    }



    public String sendRequestToServers() {
        HelperFunctions hp = new HelperFunctions();
        try {
            socket = new DatagramSocket();
            String[] divides = hp.divideToDomains(size, readyServers.size());
            for (int i = 0 ; i < readyServers.size() ;i ++) {
                InetAddress ip = readyServers.get(i);
                buf = new Message(teamName, Type.REQUEST, hashS, (char)size, divides[i*2], divides[i*2+1]).toSend().getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, UDP_PORT);
                socket.send(packet);
            }
            int counter = 0;
            socket.setSoTimeout(ttl);
            while (counter < readyServers.size()) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                if (packet.getData()[32] == 52) {
                    socket.close();
                    return new String(packet.getData()).substring(74, 74 + size);
                } else if (packet.getData()[32] == 53) {
                    counter++;
                }
            }
            socket.close();
            return null;
        } catch (IOException e) {
            System.out.println("Client ACK/NACK Socket timed out, want to try again?(y/n)");
            Scanner input = new Scanner(System.in);
            String answer = input.next();
            if (answer.equals("y")) {
                sendRequestToServers();
            }
            socket.close();
            return null;
        }
    }

    private InetAddress getBroadCastIP() {
        try {
            InetAddress localIP=InetAddress.getLocalHost();
            String add = localIP.getHostAddress();
            String[] localFrag = add.split("\\x2e");
            return InetAddress.getByName(localFrag[0]+"."+localFrag[1]+"."+localFrag[2]+".255");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getHintForTeamName() {
        return "base64";
    }
}
