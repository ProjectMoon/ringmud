package ring.skills;

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
import java.util.*;

public class SkillList implements Serializable {
    public static final long serialVersionUID = 1;
  //This is a class that represents a list of skills...

  //The actual list of Skills.
  private Vector skillList;

  public SkillList() {
    skillList = new Vector();
  }

  //addSkill method.
  //Adds a skill to the list, assuming it doesn't exist already!
  public boolean addSkill(Skill skill) {
    if (skillList.contains(skill)) return false;
    else skillList.addElement(skill);

    return true;
  }

  //removeSkill method.
  //Removes a skill from the list, assuming it exists in the list.
  public boolean removeSkill(Skill skill) {
    if (skillList.contains(skill)) {
      skillList.remove(skill);
      return true;
    }
    else return false;
  }

  //getSkillByName method.
  //This method returns a Skill based on a name passed to it. This is one of the most used methods.
  //It is case insensitive
  public Skill getSkillByName(String name) {
    //Does the spellList even have any spells in it?
    if (skillList.size() == 0)return null;

    //Guess it does!
    Skill s;
    for (int c = 0; c < skillList.size(); c++) {
      s = (Skill) skillList.get(c);
      if (name.equalsIgnoreCase(s.getName()))return s;
    }

    return null;
  }

  //hasSkill method.
  //Returns a boolean stating whether this SkillList has the specified skill.
  public boolean hasSkill(Skill skill) {
    Skill s;
    for (int c = 0; c < skillList.size(); c++) {
      s = (Skill)skillList.get(c);
      if (s.equals(skill)) return true;
    }

    //Nothing was found, so there is no skill in the list like that.
    return false;
  }


}
