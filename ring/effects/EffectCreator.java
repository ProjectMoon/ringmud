package ring.effects;

import java.io.Serializable;

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

public abstract class EffectCreator implements Serializable {
    public static final long serialVersionUID = 1;
  //This is the base class for anything that creates an effect. It used to be an interface, but I made it an
  //abstract class because it makes more sense this way; it allows me to force certain methods to be consistent across
  //all variations of the EffectCreator. EffectCreators are tied closely to the sister class EffectCreatorParameters.
  //The EffectCreatorParameters class encapsulates parameters to EffectCreators. It was done in an effort to standardize
  //the constructors of all EffectCreators as well as make initialization of Effects more dynamic.

  //This is the set of parameters for this effect creator.
  protected EffectCreatorParameters params;
  protected boolean parametersSet;

  public EffectCreator() { parametersSet = false; params = new EffectCreatorParameters(); }

  //default access modifier so that only this class and the package can use it. EffectCreators should NEVER be setting their
  //own parameters. Only the Effect class should be doing it.
  final boolean setParameters(EffectCreatorParameters newParams) {
    if (parametersSet) {
      if (params.equals(newParams)) return false;
    }

    params = newParams;
    parametersSet = true;
    return true;
  }

  //Abstract methods.
  //These must be overriden by deriving classes.
  //effectLife is called when the effect is added to a WorldObject.
  public abstract void effectLife(Affectable target);
  //effectDeath is called when the effect is removed or reaches its natural duration end.
  public abstract void effectDeath(Affectable target);
}
