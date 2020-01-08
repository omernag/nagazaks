public class Message {

    private String teamName;
    private Type type;
    private String hash;
    private char originalLength;
    private String originalStringStart;
    private String originalStringEnd;

    public Message(String teamName, Type type, String hash, int originalLength, String originalStringStart, String originalStringEnd) {
        this.teamName = teamName;
        this.type = type;
        this.hash = hash;
        String s = ""+originalLength;
        this.originalLength = s.charAt(0);
        this.originalStringStart = originalStringStart;
        this.originalStringEnd = originalStringEnd;
    }


    public String toSend(){
        return teamName+getTypeInt()+hash+originalLength+originalStringStart+originalStringEnd;
    }

    private String getTypeInt() {
        if(type==Type.DISCOVER){
            return "1";
        }
        else if(type==Type.OFFER){
            return "2";
        }
        else if(type==Type.REQUEST){
            return "3";
        }
        else if(type==Type.ACK){
            return "4";
        }
        else return "5";
    }
}
