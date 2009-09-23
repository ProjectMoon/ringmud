package ring.effects;

/**
 * <p>Title: RingMUD Codebase</p>
 *
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar
 * to DikuMUD</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: RaiSoft/Thermetics</p>
 *
 * @author Jeff Hair
 * @version 1.0
 */

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents an "effect.: The effect could be part of a spell,
 * a skill, magic items, or various other things. An Effect encapsulates
 * one or more EffectCreators (which contain the actual modifications to the
 * Affectable target) and the rules on how the effect is applied and removed.
 * @author projectmoon
 *
 */
public class Effect implements Serializable {
	public static final long serialVersionUID = 1;

	// Target of this effect.
	Affectable target;

	//The effect creators and their parameters.
	private HashMap<String, EffectCreator> effectCreators;
	private HashMap<String, EffectCreatorParameters> parameterList;

	//Has the effect begun?
	private boolean begun;
	
	// Duration variable.
	private Duration duration;

	// Timer variable... Measured in ticks.
	private int timer;

	// Is this effect supposed to be removed?
	private boolean dead;

	// Duration enum: Helps measure ... duration.
	public enum Duration {
		INSTANT, // this effect is instant
		TIMED, // this effect has a timed duration: see private int timer
		PERIODIC_TIMED, // this effect has a timed duration, but it calls
						// effectLife() every time the effect is processed.
		PERMANENT; // this effect is permanent until removed through external
					// means. usually used by items.
	}

	private int numStarts; // keeps track of # times startEffect was called.
							// used to properly remove periodic effects.

	// this boolean allows setting and checking of some attributes to further
	// help describe this effect
	// certain attributes are set automatically depending on the constructor:
	// * if target is not specified, INIT_TARGET_LATER is set.
	// * if timer is set to zero on a TIMED effect, INIT_TIMER_LATER is set.
	// * REMOVE_ON_DEATH is set automatically unless specified otherwise.

	public boolean INIT_TARGET_LATER = false;
	public boolean INIT_TIMER_LATER = false;
	public boolean INIT_EFFECT_LATER = false;
	public static final int SET_TIMER_LATER = 0;

	/**
	 * Creates a new instant Effect with no EffectCreators.
	 */
	public Effect() {
		initDefaults();
	}
	
	/**
	 * Initializes default effect information.
	 */
	private void initDefaults() {
		effectCreators = new HashMap<String, EffectCreator>();
		parameterList = new HashMap<String, EffectCreatorParameters>();
		
		dead = false;
		begun = false;
		duration = Duration.INSTANT;
		timer = 0;
		numStarts = 0;
	}
	
	public Effect(Duration dur, int timer, Affectable target, EffectCreator... efcs) {
		initDefaults();
		
		if (efcs != null) {
			for (EffectCreator ef : efcs)
				effectCreators.put(ef.toString(), ef);
		}
		
		this.timer = timer;
		this.target = target;
		duration = dur;
		dead = false;
		INIT_TARGET_LATER = false;
		numStarts = 0;
		if ((dur == Duration.TIMED) && (timer == 0))
			INIT_TIMER_LATER = true;
	}

	public Effect(Duration dur, int timer, EffectCreator... efcs) {
		initDefaults();

		if (efcs != null) {
			for (EffectCreator ef : efcs)
				effectCreators.put(ef.toString(), ef);
		}
		
		this.timer = timer;
		target = null;
		duration = dur;
		dead = false;
		INIT_TARGET_LATER = true;
		numStarts = 0;
		if ((dur == Duration.TIMED) && (timer == 0))
			INIT_TIMER_LATER = true;
	}

	// This copy constructor is used when starting up effects in order to create
	// a new, unique
	// instance. This prevents strange timing issues when applying the same
	// effect multiple times
	// at once. It is up to the Effect-encapsulating classes (Spell,
	// ClassFeature, Item, etc) to deal
	// with this issue. They call uniqueInstance() to do this.
	private Effect(Effect other) {
		effectCreators = other.effectCreators;
		parameterList = other.parameterList;
		timer = other.timer;
		target = other.target;
		duration = other.duration;
		dead = other.dead;
		INIT_TARGET_LATER = other.INIT_TARGET_LATER;
		INIT_EFFECT_LATER = other.INIT_EFFECT_LATER;
		INIT_TIMER_LATER = other.INIT_TIMER_LATER;
		numStarts = 0; // this is a new effect, so numStarts is still zero.
	}

	public void addEffectCreator(String efcKey, EffectCreator efc) {
		effectCreators.put(efcKey, efc);
	}

	public void decrementTimer() {
		// ignore permanent duration effects. they are removed other ways.
		if (duration.equals(Duration.PERMANENT))
			return;

		if (timer <= 0) {
			endEffect();
			return;
		} 
		else {
			// if this is a periodic effect, make it happen again!
			if (duration == Duration.PERIODIC_TIMED)
				startEffect();

			timer--;
		}
	}

	// setTimer method.
	// This allows the timer to be set to some postive number. It can only be
	// set if INIT_TIME_LATER is set to true.
	// After it is set, INIT_TIME_LATER goes false and can no longer be set.
	// Returns true if successful,
	// false otherwise.
	public boolean setTimer(int n) {
		if (INIT_TIMER_LATER == true && n >= 0) {
			timer = n;
			INIT_TIMER_LATER = false;
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isDead() {
		return dead;
	}
	
	/**
	 * Begins the execution of this effect. Calls to
	 * begin() only work once. Subsequent calls are silently ignored.
	 */
	public void begin() {
		if (!begun) {
			passParameters();
			startEffect();
			begun = true;
		}
	}
	
	private void startEffect() {
		numStarts++;
		if (duration == Duration.INSTANT)
			dead = true; // dead on arrival?
		for (EffectCreator ef : effectCreators.values())
			ef.effectLife(target);
	}

	public void endEffect() {
		dead = true;
		for (int c = 0; c < numStarts; c++)
			// call effectDeath a number of times equal to the number of
			// effectLife calls.
			for (EffectCreator ef : effectCreators.values())
				ef.effectDeath(target);

		numStarts = 0; // reset the numStarts counter for the next time this
						// Effect is enacted.
	}

	// uniqueInstance method.
	// This returns a unique instance of this Effect. Gets rid of timing issues
	// and many other things.
	public Effect uniqueInstance() {
		return new Effect(this);
	}
	
	/**
	 * Adds a parameter to the specified EffectCreator (by key).
	 * @param efcKey
	 * @param paramName
	 * @param value
	 * @return
	 */
	public boolean addParameter(String efcKey, String paramName, Object value) {
		EffectCreatorParameters params = parameterList.get(efcKey);
		
		if (params == null) {
			params = new EffectCreatorParameters();
		}
		
		params.add(paramName, value);
		parameterList.put(efcKey, params);
		
		return true;
	}

	// passParameters method.
	// This method passes parameters to all EffectCreators in this Effect.
	// Returns true
	// if succesful in adding parameters to all EffectCreators, but false if one
	// fails.
	private void passParameters() {
		for (String efcKey : effectCreators.keySet()) {
			EffectCreatorParameters params = parameterList.get(efcKey);
			EffectCreator efc = effectCreators.get(efcKey);
			efc.setParameters(params);
		}
	}

	public Affectable getTarget() {
		return target;
	}

	public void setTarget(Affectable target) {
		this.target = target;
	}

	public Duration getDuration() {
		return duration;
	}

	public String toString() {
		String text = "[Effect]: ";
		for (String ef : effectCreators.keySet()) {
			text += ef.toString() + ";";
		}

		return text;
	}
}
