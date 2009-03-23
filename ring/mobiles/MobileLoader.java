package ring.mobiles;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class serves as a way to load and save mobiles. Will be merged into bigger class
 * later.
 * @author jeff
 */
public class MobileLoader {
    public static Mobile loadMobile(String filename) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filename));
            Mobile mob = (Mobile) in.readObject();
            in.close();
            mob.init();
            return mob;
        } catch (IOException ex) {
            Logger.getLogger(MobileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException e) { 
            e.printStackTrace(); 
        }
        finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(MobileLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
    
    public static void saveMobile(String filename, Mobile mob) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(mob);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(MobileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
