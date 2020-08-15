package ring.magic;

import ring.effects.Affectable;
import ring.effects.Effect;

/**
 * Class representing a magical spell. This class is purposely minimal;
 * it is up to the MagicSystem implementation to interpret the metadata
 * stored in the Spell object and make use of it.
 *
 * @author projectmoon
 */
public class Spell {
    private String name;
    private String description;
    private SpellMetadata metadata;

    private Effect effect;
    private String source;
    private Affectable target;
    private TargetingRule targetingRule;
    private String targetingRuleClass;


    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    /**
     * Convenience method for checking the targeting rule. Same as
     * calling getTargetingRule().isTargetAllowed(Affectable).
     *
     * @param target
     * @return true if the spell can be cast at the target, false otherwise.
     */
    public boolean isTargetAllowed(Affectable target) {
        return getTargetingRule().isTargetAllowed(target);
    }


    public Affectable getTarget() {
        return target;
    }

    public void setTarget(Affectable target) {
        this.target = target;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public TargetingRule getTargetingRule() {
        return targetingRule;
    }

    public void setTargetingRule(TargetingRule rule) {
        targetingRule = rule;
    }

    public String getTargetingRuleClass() {
        return targetingRuleClass;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }


    public SpellMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(SpellMetadata metadata) {
        this.metadata = metadata;
    }

    public void setTargetingRuleClass(String className) {
        try {
            Class<?> c = Class.forName(className);
            TargetingRule rule = (TargetingRule) c.newInstance();
            setTargetingRule(rule);
        } catch (ClassNotFoundException e) {
            //TODO log exceptions for setTargetingRuleClass.
        } catch (InstantiationException e) {

        } catch (IllegalAccessException e) {

        }
    }
}