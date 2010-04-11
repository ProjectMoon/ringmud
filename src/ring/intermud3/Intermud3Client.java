package ring.intermud3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ring.deployer.DeployedMUDFactory;
import ring.players.Player;

import com.aelfengard.i3.ChanInfo;
import com.aelfengard.i3.ErrorCallback;
import com.aelfengard.i3.I3ChannelListener;
import com.aelfengard.i3.I3Client;
import com.aelfengard.i3.I3EventListener;
import com.aelfengard.i3.I3NotConnectedException;
import com.aelfengard.i3.LPCMixed;
import com.aelfengard.i3.MudInfo;
import com.aelfengard.i3.packet.ChannelEPacket;
import com.aelfengard.i3.packet.ChannelMPacket;
import com.aelfengard.i3.packet.ChannelTPacket;
import com.aelfengard.i3.packet.EmoteToPacket;
import com.aelfengard.i3.packet.ErrorPacket;
import com.aelfengard.i3.packet.TellPacket;

/**
 * A per-player Intermud3 client built on top of the i3.jar library.
 * @author projectmoon
 *
 */
public class Intermud3Client {
	private static I3Client client = Intermud3Daemon.currentDaemon().getClient();
	
	private Player player;
		
	public Intermud3Client(Player player) {
		this.player = player;
		client.addEventListener(new I3EventsListener());
	}
	
	public boolean isConnected() {
		return client.isConnected();
	}
	
	public void sendTell(String toMud, String toUser, String message) {
		TellPacket tpacket = new TellPacket();
		tpacket.setOriginatorMudName(new LPCMixed(DeployedMUDFactory.currentMUD().getName()));
		tpacket.setOriginatorUsername(new LPCMixed(player.getName()));
		tpacket.setOrigVisName(new LPCMixed(player.getName()));
		tpacket.setTargetMudName(new LPCMixed(toMud));
		tpacket.setTargetUsername(new LPCMixed(toUser));
		tpacket.setMessage(new LPCMixed(message));

		try {
			client.send(tpacket);
			//For receiving tells, see the event handler below.
		} 
		catch (I3NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void submitWhoRequest(String mudName) throws I3NotConnectedException {
		client.sendWho(new LPCMixed(player.getName()), new LPCMixed(mudName));
		//Rest is handled by the event handler.
	}
	
	public void sendEmote(String toMud, String toUser, String message) {
		
	}
	
	public void joinChannel(String channel) {
		
	}
	
	public void leaveChannel(String channel) {
		
	}
	
	public List<String> getMuds() {
		List<String> muds = new ArrayList<String>();
        for (Map.Entry<String,MudInfo> entry : client.getMudList().entrySet()) {
            if (entry.getValue().getState().asInteger() == MudInfo.MUD_STATE_UP) {
                muds.add(entry.getKey());
            }
        }
        
        return muds;
	}
	
	public List<String> getChannels() {
		List<String> channels = new ArrayList<String>();
        for (Map.Entry<String,ChanInfo> entry : client.getChannelList().entrySet()) {
            if (entry.getValue().getChannelType() == ChanInfo.CHANNEL_TYPE_SELECTIVELY_BANNED) {
                channels.add(entry.getKey());
            }
        }
        
        return channels;
	}

	private class I3EventsListener implements I3EventListener {
	    public void whoReply(LPCMixed targetUsername, LPCMixed originatorMudName, List<LPCMixed> whoInfo) {
	    	System.out.println("Testing vs " + player.getName());
	    	if (player.getName().equalsIgnoreCase(targetUsername.asString())) {
	    		String list = "Players on [B]" + originatorMudName.asString() + "[R]:\n";
	    		
		        for (LPCMixed info : whoInfo) {
		            List<LPCMixed> entry = info.asList();
		            
		            list += (entry.get(0) + " [" + entry.get(1) + "/" + entry.get(2) + "]") + "\n";
		            
		            player.getSystemMessageHandler().sendMessage(list);
		        }	    		
	    	}
	    }

	    public void whoFailed(LPCMixed targetUsername, LPCMixed targetMudName, LPCMixed errorMessage) {
	        System.out.println("WHO failed: " + errorMessage.asString());
	    }

	    public List<LPCMixed> whoRequest() {
	    	return null;
	    }

	    public void tell(TellPacket packet, ErrorCallback callback) {
	        if (player.getName().equals(packet.getTargetUsername().asString())) {
	        	String message = "[B]" + packet.getOriginatorUsername() + "@" + packet.getOriginatorMudName() + "[R] tells you: " + packet.getMessage();
	            player.getSystemMessageHandler().sendMessage(message);
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
	    	/*
	        if ("some guy".equals(packet.getTargetUsername().asString())) {
	            String theirName = packet.getOriginatorUsername() + "@" + packet.getOriginatorMudName();
	            System.out.println(packet.getMessage().asString().replace("$N", theirName));
	        }
	        else {
	            callback.returnError("Unknown user");
	        }
	        */
	    	//TODO implement emotes.
	    }
	}
}

class I3ChannelsListener implements I3ChannelListener {

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
