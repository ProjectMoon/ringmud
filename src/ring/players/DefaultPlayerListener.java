package ring.players;

import ring.events.listeners.MobileEvent;
import ring.events.listeners.MobileListener;

public class DefaultPlayerListener implements MobileListener {

	@Override
	public void dequippedItem(MobileEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void droppedItem(MobileEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void equippedItem(MobileEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mobileAttacked(MobileEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mobileFleed(MobileEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mobileLooked(MobileEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mobileMoved(MobileEvent e) {
		System.out.println(e.getSource() + " moved.");
	}

	@Override
	public void mobileReceivedAttack(MobileEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickedUpItem(MobileEvent e) {
		// TODO Auto-generated method stub
		
	}

}
