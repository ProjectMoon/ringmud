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
 * Class used to encapsulate effect creator parameters. Each
 * EffectCreator stores its own set of parameters. Cannot be
 * instantiated directly outside the package.
 * @author projectmoon
 *
 */
public final class EffectCreatorParameters implements Serializable {
	public static final long serialVersionUID = 1;
	// This class encapsulates EffectCreator parameters. They are stored as a
	// HashMap, with string names for keys.
	private HashMap<String, Object> params;

	protected EffectCreatorParameters() {
		params = new HashMap<String, Object>();
	}

	protected EffectCreatorParameters(HashMap<String, Object> params) {
		this.params = params;
	}

	protected EffectCreatorParameters(EffectCreatorParameters other) {
		this.params = new HashMap<String, Object>(other.params);
	}

	// begin add methods.
	// There is a version of add for every pertinent primitive type, Strings,
	// and one for a general catch-all Object.
	public void add(String name, int i) {
		Integer in = new Integer(i);
		params.put(name, in);
	}

	public void add(String name, boolean b) {
		Boolean bo = new Boolean(b);
		params.put(name, bo);
	}

	public void add(String name, double d) {
		Double dou = new Double(d);
		params.put(name, dou);
	}

	public void add(String name, String s) {
		params.put(name, s);
	}

	public void add(String name, Object o) {
		params.put(name, o);
	}

	// begin get methods
	// There is a version of get for eveyr pertinent primitive type, Strings,
	// and one for a general catch-all Object.
	public int getInt(String name) {
		Integer i = (Integer) params.get(name);
		if (i == null)
			return 0;
		return i.intValue();
	}

	public boolean getBoolean(String name) {
		Boolean bo = (Boolean) params.get(name);
		if (bo == null)
			return false;
		return bo.booleanValue();
	}

	public double getDouble(String name) {
		Double dou = (Double) params.get(name);
		if (dou == null)
			return 0.0;
		return dou.doubleValue();
	}

	public String getString(String name) {
		String s = (String) params.get(name);
		if (s == null)
			return "";
		return s;
	}

	public Object getObject(String name) {
		return params.get(name);
	}

	public void removeParameter(String name) {
		params.remove(name);
	}

	public boolean equals(EffectCreatorParameters other) {
		return params.equals(other.params);
	}

	public EffectCreatorParameters uniqueInstance() {
		return new EffectCreatorParameters(this);
	}
}
