package ring.intermud3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ring.players.Player;
import ring.server.MUDConnectionManager;

import com.aelfengard.i3.ErrorCallback;
import com.aelfengard.i3.I3EventListener;
import com.aelfengard.i3.LPCMixed;
import com.aelfengard.i3.packet.EmoteToPacket;
import com.aelfengard.i3.packet.ErrorPacket;
import com.aelfengard.i3.packet.TellPacket;

class I3SystemEventListener implements I3EventListener {


    /**
     * Respond to who requests.
     */
    public List<LPCMixed> whoRequest() {
    	List<Player> players = MUDConnectionManager.getCurrentPlayers();
    	
        List<LPCMixed> myInfo = new ArrayList<LPCMixed>();
        
        for (Player p : players) {
	        myInfo.add(new LPCMixed(p.getName()));
	        myInfo.add(new LPCMixed(-1));
	        myInfo.add(new LPCMixed("I3J User"));
        }
        
        return Arrays.asList(new LPCMixed[] { new LPCMixed(myInfo) });
    }
    
    /**
     * Log error messages.
     */
    public void i3Error(ErrorPacket packet) {
        System.out.println("I3 ERROR: [" + packet.getErrorCode() + "] " + packet.getErrorMessage());
    }

    public void tell(TellPacket packet, ErrorCallback callback) {

    }

    public void tellFailed(LPCMixed username, LPCMixed targetMudName, LPCMixed targetUsername, LPCMixed errorMessage) {
    	
    }

    public void whoReply(LPCMixed targetUsername, LPCMixed originatorMudName, List<LPCMixed> whoInfo) {

    }

    public void whoFailed(LPCMixed targetUsername, LPCMixed targetMudName, LPCMixed errorMessage) {

    }


    public void emoteTo(EmoteToPacket packet, ErrorCallback callback) {

    }

}

