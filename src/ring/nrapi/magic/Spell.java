package ring.nrapi.magic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ring.effects.Affectable;
import ring.effects.Effect;
import ring.nrapi.data.RingConstants;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(
namespace = RingConstants.RING_NAMESPACE,
propOrder= {
	"targetingRuleClass",
	"metadata",
	"effect",
	"source"
})
/**
 * Class representing a magical spell. This class is purposely minimal;
 * it is up to the MagicSystem implementation to interpret the metadata
 * stored in the Spell object and make use of it.
 * @author projectmoon
 *
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
	
	@XmlElement
	public Effect getEffect() {
		return effect;
	}
	
	public void setEffect(Effect effect) {
		this.effect = effect;
	}
	
	/**
	 * Convenience method for checking the targeting rule. Same as
	 * calling getTargetingRule().isTargetAllowed(Affectable).
	 * @param target
	 * @return true if the spell can be cast at the target, false otherwise.
	 */
	public boolean isTargetAllowed(Affectable target) {
		return getTargetingRule().isTargetAllowed(target);
	}
	
	@XmlTransient
	public Affectable getTarget() {
		return target;
	}
	
	public void setTarget(Affectable target) {
		this.target = target;
	}
	
	@XmlElement
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	@XmlTransient
	public TargetingRule getTargetingRule() {
		return targetingRule;
	}
	
	public void setTargetingRule(TargetingRule rule) {
		targetingRule = rule;
	}
	
	@XmlAttribute(name = "targetRule")
	public String getTargetingRuleClass() {
		return targetingRuleClass;
	}
	

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}
	
	@XmlElement
	public SpellMetadata getMetadata() {
		return metadata;
	}
	
	public void setMetadata(SpellMetadata metadata) {
		this.metadata = metadata;
	}
	
	public void setTargetingRuleClass(String className) {
		try {
			Class<?> c = Class.forName(className);
			TargetingRule rule = (TargetingRule)c.newInstance();
			setTargetingRule(rule);
		}
		catch (ClassNotFoundException e) {
			//TODO log exceptions for setTargetingRuleClass.
		}
		catch (InstantiationException e) {

		}
		catch (IllegalAccessException e) {

		}
	}
}