package ring.effects;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */

//An interface that allows objects that implement it to interact with the MUD world.
//Anything considered "Affectable" can be hit with effects. Of course, individual effects
//and commands might have their own targeting restrictions.
//
//An Affectable object is considered to be in the world and thus has HP and AC.
public interface Affectable {
  public static final int LIVING = 0;
  public static final int NON_LIVING = 1;

  public String getLongDescription();
  public Object source();
  public boolean isPlayer();
  public boolean isNPC();
  public String getName();
  public int getEntityType();
  public int getCurrentHP();
  public int getMaxHP();
  public int getAC();
  public void addEffect(Effect e);
}
