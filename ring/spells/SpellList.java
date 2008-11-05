package ring.spells;

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

import java.util.*;
import ring.effects.*;

public class SpellList {
  //This class defines a list of spells available to a MobileClass. It also defines the spell
  //levels for the spells. This allows some classes to receive spells later while others get
  //them sooner... Or something.

  //Instance variables.
  private Vector spellList;


  public SpellList() {
    spellList = new Vector();
  }

  //getSpellByName method.
  //This method returns a Spell based on a name passed to it. This is one of the most used methods.
  public Spell getSpellByName(String name) {
    //Does the spellList even have any spells in it?
    if (spellList.size() == 0) return null;

    //Guess it does!
    Spell s;
    for (int c = 0; c < spellList.size(); c++) {
      s = (Spell)spellList.get(c);
      if (name.equalsIgnoreCase(s.getName())) return s;
    }

    return null;
  }

  //addSpell method.
  //This method adds a spell to the list using the spell's default level. It returns a boolean depending
  //on whether it successfully adds it or not (no clones of spells!)
  public boolean addSpell(Spell spell) {
    if (spellList.contains(spell)) return false;
    else spellList.addElement(spell);

    return true;
  }

  //addSpell method: The custom level verison.
  //This method adds a spell to the list using the spell's specified level. It returns a boolean depending
  //on whether it successfully adds it or not (no clones of spells!)
  public boolean addSpell(Spell spell, int level) {
    spell.setLevel(level);
    if (spellList.contains(spell)) return false;
    else spellList.addElement(spell);

    return true;
  }


  //removeSpell method.
  //This method should never be called... The logger will log all calls of it.
  public boolean removeSpell(Spell spell) {
    if(spellList.contains(spell)) {
      spellList.remove(spell);
      return true;
    }

    return false;
  }

  //getSpellsByLevel method.
  //Returns a Spell[] depending on the level specified. IMPLEMENT LATER !! ...Maybe.

  //castAll method.
  //This method is USED FOR ITEMS ONLY!! It simply loops through all of the spells on the list
  //and calls their cast method...
  public void castAll(Affectable target) {
    //Are there any spells to cast.... ?
    if (spellList.size() == 0) return;

    Spell s;
    for (int c = 0; c < spellList.size(); c++) {
      s = (Spell)spellList.get(c);
      Effect e = s.generateEffect();
      e.setTarget(target);
      e.startEffect();
    }
  }

  //decastAll method.
  //This method is USED FOR ITEMS ONLY!! It is the inverse of above.
  public void decastAll(Affectable target) {
    //Are there any spells to cast.... ?
    if (spellList.size() == 0) return;

    Spell s;
    for (int c = 0; c < spellList.size(); c++) {
      s = (Spell)spellList.get(c);
      Effect e = s.generateEffect();
      e.setTarget(target);
      e.endEffect();
    }
  }

  public String toString() {
    String res = "";
    for (int c = 0; c < spellList.size(); c++) {
      res += ((Spell)(spellList.get(c))).getName() + " || ";
    }

    return res;
  }

}
