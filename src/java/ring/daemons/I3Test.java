package ring.daemons;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.aelfengard.i3.ChanInfo;
import com.aelfengard.i3.ErrorCallback;
import com.aelfengard.i3.I3ChannelListener;
import com.aelfengard.i3.I3Client;
import com.aelfengard.i3.I3EventListener;
import com.aelfengard.i3.LPCMixed;
import com.aelfengard.i3.MudInfo;
import com.aelfengard.i3.packet.ChannelEPacket;
import com.aelfengard.i3.packet.ChannelMPacket;
import com.aelfengard.i3.packet.ChannelTPacket;
import com.aelfengard.i3.packet.EmoteToPacket;
import com.aelfengard.i3.packet.ErrorPacket;
import com.aelfengard.i3.packet.TellPacket;

public class I3Test {

    private static final String MY_USERNAME = "TestUser";
    private static final String CHANNEL = "spam";
    
    public static void main(String... args) throws Exception {
    	System.out.println("Connecting...");
        I3Client client = new I3Client();
        client.setRouterName("*i4");
        client.setHost("204.209.44.3");
        client.setPort(8080);
        
        client.setMudName("RingMUD Test");
        client.setAdminEmail("projectmoon@ringmud");
        client.addEventListener(new MyI3EventListener());
        client.addChannelListener(CHANNEL, new MyI3ChannelListener());
        client.connect(); // use autoreconnect() if you don't want to block
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = "help";
        while (true) {
            int idx = line.indexOf(' ');
            String token = idx < 0 ? line : line.substring(0, idx);
            String rest = idx < 0 ? null : line.substring(idx + 1);
            if (token.equalsIgnoreCase("help")) {
                System.out.println("Commands:");
                System.out.println("help");
                System.out.println("muds");
                System.out.println("channels");
                System.out.println("say <message>");
                System.out.println("who <mudname>");
                System.out.println("quit");
            }
            else if (token.equalsIgnoreCase("muds")) {
                for (Map.Entry<String,MudInfo> entry : client.getMudList().entrySet()) {
                    if (entry.getValue().getState().asInteger() == MudInfo.MUD_STATE_UP) {
                        System.out.println(entry.getKey());
                    }
                }
            }
            else if (token.equalsIgnoreCase("channels")) {
            	System.out.println("Lookin fer channelz");
                for (Map.Entry<String,ChanInfo> entry : client.getChannelList().entrySet()) {
                    if (entry.getValue().getChannelType() == ChanInfo.CHANNEL_TYPE_SELECTIVELY_BANNED) {
                        System.out.println(entry.getKey());
                    }
                }
            }
            else if (token.equalsIgnoreCase("say")) {
                if (rest == null) {
                    System.out.println("You didn't specify anything to say.");
                }
                else {
                    ChannelMPacket packet = new ChannelMPacket();
                    packet.setChannelName(new LPCMixed(CHANNEL));
                    packet.setMessage(new LPCMixed(rest));
                    packet.setOriginatorUsername(new LPCMixed(MY_USERNAME.toLowerCase()));
                    packet.setVisName(new LPCMixed(MY_USERNAME));
                    client.send(packet);
                }
            }
            else if (token.equalsIgnoreCase("who")) {
                if (rest == null) {
                    System.out.println("You didn't specify a mud name.");
                }
                else {
                    client.sendWho(new LPCMixed(MY_USERNAME.toLowerCase()), new LPCMixed(rest));
                }
            }
            else if ("quit".equalsIgnoreCase(token)) {
                System.exit(0);
            }
            else {
                System.out.println("Unknown command: " + token);
            }
            line = in.readLine();
            if (line == null) {
                break;
            }
        }
    }
    
    private static class MyI3EventListener implements I3EventListener {

        public void whoReply(LPCMixed targetUsername, LPCMixed originatorMudName, List<LPCMixed> whoInfo) {
            for (LPCMixed info : whoInfo) {
                List<LPCMixed> entry = info.asList();
                System.out.println(entry.get(0) + " [" + entry.get(1) + "/" + entry.get(2) + "]");
            }
        }

        public void whoFailed(LPCMixed targetUsername, LPCMixed targetMudName, LPCMixed errorMessage) {
            System.out.println("WHO failed: " + errorMessage.asString());
        }

        public List<LPCMixed> whoRequest() {
            List<LPCMixed> myInfo = new ArrayList<LPCMixed>();
            myInfo.add(new LPCMixed(MY_USERNAME));
            myInfo.add(new LPCMixed(-1));
            myInfo.add(new LPCMixed("I3J User"));
            return Arrays.asList(new LPCMixed[] { new LPCMixed(myInfo) });
        }

        public void tell(TellPacket packet, ErrorCallback callback) {
            if (MY_USERNAME.equals(packet.getTargetUsername().asString())) {
                System.out.println(packet.getOriginatorUsername() + "@" + packet.getOriginatorMudName() + " tells you: " + packet.getMessage());
            }
            else {
                callback.returnError("Unknown user");
            }
        }

        public void tellFailed(LPCMixed username, LPCMixed targetMudName, LPCMixed targetUsername, LPCMixed errorMessage) {
            System.out.println("TELL FAILED: " + errorMessage.asString());
        }

        public void i3Error(ErrorPacket packet) {
            System.out.println("I3 ERROR: [" + packet.getErrorCode() + "] " + packet.getErrorMessage());
        }

        public void emoteTo(EmoteToPacket packet, ErrorCallback callback) {
            if (MY_USERNAME.equals(packet.getTargetUsername().asString())) {
                String theirName = packet.getOriginatorUsername() + "@" + packet.getOriginatorMudName();
                System.out.println(packet.getMessage().asString().replace("$N", theirName));
            }
            else {
                callback.returnError("Unknown user");
            }
        }
        
    }
    
    private static class MyI3ChannelListener implements I3ChannelListener {

        public void i3Message(ChannelMPacket packet) {
            String theirName = packet.getVisName() + "@" + packet.getOriginatorMudName();
            System.out.println(theirName + ": " + packet.getMessage());
        }

        public void i3Message(ChannelEPacket packet) {
            String theirName = packet.getVisName() + "@" + packet.getOriginatorMudName();
            String msg = packet.getMessage().asString().replace("$N", theirName);
            System.out.println(theirName + ": " + msg);
        }

        public void i3Message(ChannelTPacket packet) {
            String theirName = packet.getOriginatorVisName() + "@" + packet.getOriginatorMudName();
            String targetName = packet.getTargetVisName() + "@" + packet.getTargettedMudName();
            String msg = packet.getMessageOthers().asString().replace("$N", theirName).replace("$O", targetName);
            System.out.println(theirName + ": " + msg);
        }

        public void channelRemoved(String channel, LPCMixed reason) {
            System.out.println("You've been removed from " + channel + ": " + reason.asString());
        }
        
    }
    
}
