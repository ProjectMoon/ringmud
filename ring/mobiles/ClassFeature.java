package ring.mobiles;

import java.util.*;
import ring.effects.*;

/**
 * A complete re-write of the ClassFeature class. A class feature needs several
 * things. One, it needs a 
 * @author jeff
 */
public class ClassFeature {
    //Constants
    public static String SELF_ONLY = "self";
    public static String OTHERS_ONLY = "others";
    public static String ALL = "all";
    
    //The effects of this class. Stored as such for scaling features that change
    //with level.
    private HashMap<Integer, Effect> featureEffects;
    
    //The feature we should be using; done so we don't have to continually look
    //for the right level feature.
    private int featureToUse;
    
    //Self-explanatory.
    private String name;
    private String command; //The command used to execute this feature--may or may not take params
    private String description;
    private String targetType; //self, others, all. These values are exclusive; i.e. others does not allow self-target.
    private String outputText; //what to send back to the executor.
    
    
    public ClassFeature() {
        featureEffects = new HashMap<Integer, Effect>();
    }
    
    public ClassFeature(String name, String command, String description, String text, String targetType) {
        featureEffects = new HashMap<Integer, Effect>();
        this.name = name;
        this.command = command;
        this.description = description;
        this.targetType = targetType;
        this.outputText = text;
    }
    
    /**
     * This method figures out the right "level" of the class feature to use,
     * if it indeed has more than one level. This should not be called all the
     * time as it is fairly inefficient. It gets called automatically during
     * the creation of a Mobile and when a player levels up.
     */
    public void chooseFeature(int mobLevel) {
        //first test to see if there's a feature right at this level.
        Effect eff = featureEffects.get(mobLevel);
        int featureLevel = mobLevel;
        
        //If not, we need to find it.
        //We find the closest-level feature effect that is equal to or lower
        //than the specified level.
        if (eff == null) {
            for (featureLevel = mobLevel; featureLevel > 0; featureLevel--) {
                eff = featureEffects.get(featureLevel);
                if (eff != null) break;
            }   
        }
        
        //Now, we should've found it. If the number is zero, there are
        //other problems we should be worrying about...
        featureToUse = featureLevel;
    }
    
    /**
     * "Executes" this class feature on the specified target. The target parameter
     * can be ignored if the target of the class feature is set to "self." If not,
     * the parameter must be passed. The check for that, however, is handled in
     * CommandHandler, not this class.
     * @param target
     * @return True on a successful application of the class feature.
     */
    public boolean execute(Affectable target) {
        Effect eff = featureEffects.get(featureToUse);
        eff.setTarget(target);
        target.addEffect(eff);
        return true;
    }
    
    public void setCommand(String cmd) {
        command = cmd;
    }
    
    public String getCommand() {
        return command;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setDescription(String desc) {
        description = desc;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void addEffect(int level, Effect eff) {
        featureEffects.put(level, eff);
    }
    
    public void removeEffect(int level) {
        featureEffects.remove(level);
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public void setTargetType(String type) {
        targetType = type;
    }
    
    public String getOutputText() {
        return outputText;
    }
    
    public void setOutputText(String text) {
        outputText = text;
    }
    
    public String toString() {
        String res = "";
        res = name + " (" + command + "); [Effects: ";
        
        Collection<Effect> c = featureEffects.values();
        for (Effect e : c)
            res += e.toString();
        
        res += "]";
        return res;
    }
}
