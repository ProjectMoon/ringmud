package ring.mobiles;

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

import ring.skills.*;
import ring.spells.*;
import ring.effects.*;
import ring.effects.library.*;
import ring.world.*;

import java.util.*;

public class MobileClassFactory {
  //This class is HOPEFULLY a placeholder of sorts. It currently contains hard-coded versions of
  //all RingMUD classes. This was made to make it much easier to assign a class to a mobile (Instead
  //of using the huge constructor).

  public MobileClassFactory() {
  }

  //chooseClassFromMenu method.
  //Returns a class based on the menu choices when creating a new character.
  /*public MobileClass chooseClassFromMenu(String choice) {
    MobileClass mc;

    return mc;
  }*/

  //makeFighter method.
  //This method creates the Warrior class.
  public static MobileClass makeFighter() {
    MobileClass res;

    //Class features.
    //bash
    Effect bashEffect = new Effect(Effect.Duration.INSTANT, 0, new KnockDown());
    ClassFeature bash = new ClassFeature("Bash", "The act of bashing one\'s opponent is the first basic maneuver that fighters learn. It is a tactical maneuver that sends an opponent sprawling. It keeps the target focused on the fighter.", bashEffect, ClassFeature.Type.SINGLE, ClassFeature.DurationType.ACTIVE, 1, 0);

    //Skills.

    //Skill list.
    SkillList skills = new SkillList();

    SpellList spells = new SpellList();

    //Make the class itself.
    res = new MobileClass("Fighter", "[BLUE]Fighter[WHITE]", 10, 2, spells,  skills,
                          MobileClass.SaveType.GOOD, MobileClass.SaveType.POOR,
                          MobileClass.SaveType.POOR, MobileClass.BaseAttackBonus.GOOD,
                          MobileClass.Classification.PHYSICAL);

    //add class features
    res.addClassFeature(bash);
    return res;
  }

  //makeBarbarian method.
  //This method makes the Barbarian class.
  public static MobileClass makeBarbarian() {
    MobileClass res;

    //Class Features.
    //rage
    Effect rageEffect = new Effect(Effect.Duration.TIMED, Effect.SET_TIMER_LATER, new StatChange(), new ACChange());
    EffectCreatorParameters rageParams = new EffectCreatorParameters();
    rageParams.add("StatChange:strength", 4);
    rageParams.add("StatChange:constitution", 4);
    rageParams.add("ACChange:amount", -2);
    rageEffect.passParameters(rageParams);

    ClassFeature rage = new ClassFeature("Rage", "Once per fight, a barbarian can fly into a rage. The barbarian temporarily gains a +4 bonus to Strength and Constitution, and a +2 morale bonus on Will saves, and takes a -2 penalty to Armor Class. The increase in Constitution increases the barbarian\'s hit points by 2 points per level, but these hit points go away at the end of the rage. While raging, a barbarian cannot use any Charisma-, Dexterity-, or Intelligence-based skills (except for Balance, Escape Artist, Intimidate, and Ride), the Concentration skill, or any abilities that require patience or concentration, nor can he activate magic items that require a command word, a spell trigger (such as a wand), or spell completion (such as a scroll) to function. He can use any feat he has except Combat Expertise, item creation feats, and metamagic feats. A fit of rage lasts for one minute. At the end of the rage, the barbarian loses the rage modifiers and restrictions and becomes fatigued (-2 penalty to Strength, -2 penalty to Dexterity, max movement halved) for the remainder of the encounter.", rageEffect, ClassFeature.Type.SINGLE, ClassFeature.DurationType.ACTIVE, 1, 0);

    Effect bashEffect = new Effect(Effect.Duration.INSTANT, 0, new KnockDown());
    ClassFeature bash = new ClassFeature("Bash", "The act of bashing one\'s opponent is the first basic maneuver that fighters learn. It is a tactical maneuver that sends an opponent sprawling. It keeps the target focused on the fighter.", bashEffect, ClassFeature.Type.SINGLE, ClassFeature.DurationType.ACTIVE, 1, 0);

    //Skill list.
    SkillList skills = new SkillList();
    Skill search = new Skill("search", "This allows you to search rooms for interesting things.", Mobile.INTELLIGENCE, 30, 4);
    Skill hide = new Skill("hide", "Sneaky!", Mobile.DEXTERITY, 30, 4);
    Skill moveSilently = new Skill("move silently", "Softly!", Mobile.DEXTERITY, 30, 4);
    Skill spot = new Skill("spot", "spotty!", Mobile.WISDOM, 30, 4);
    Skill listen = new Skill("listen", "intently!", Mobile.WISDOM, 30, 4);
    
    skills.addSkill(search);
    skills.addSkill(hide);
    skills.addSkill(moveSilently);
    skills.addSkill(spot);
    skills.addSkill(listen);
    
    Effect mme = new Effect(Effect.Duration.INSTANT, 0, new HPChange());
    EffectCreatorParameters hpe = new EffectCreatorParameters();
    hpe.add("HPChange:amount", -5);
    mme.passParameters(hpe);
    Spell mm = new Spell("magic missile", 0, mme);
    SpellList spells = new SpellList();
    spells.addSpell(mm);

    //Make the class itself.
    res = new MobileClass("Barbarian", "[B][RED]Barbarian[R][WHITE]", 12, 2, spells, skills,
                          MobileClass.SaveType.GOOD, MobileClass.SaveType.POOR,
                          MobileClass.SaveType.POOR, MobileClass.BaseAttackBonus.GOOD,
                          MobileClass.Classification.PHYSICAL);

    //add class features
    res.addClassFeature(rage);
    res.addClassFeature(bash);

    return res;

  }

  //makePaladin method.
  //Makes the Paladin class.
  public static MobileClass makePaladin() {
    MobileClass res;

    //Skills.

    //Skill list.
    SkillList skills = new SkillList();

    //Spells.

    //Spell list.
    SpellList spells = new SpellList();

    //Make the class itself.
    res = new MobileClass("Paladin", "[B][WHITE]Paladin[R][WHITE]", 10, 2, spells, skills,
                          MobileClass.SaveType.GOOD, MobileClass.SaveType.POOR,
                          MobileClass.SaveType.POOR, MobileClass.BaseAttackBonus.GOOD,
                          MobileClass.Classification.HYBRID);


    return res;

  }

  //makeCleric method.
  //Makes the Cleric class.
  public static MobileClass makeCleric() {
    MobileClass res;

    //Skills.

    //Skill list.
    SkillList skills = new SkillList();

    //Spells.

    //Spell list.
    SpellList spells = new SpellList();

    //Make the class itself.
    res = new MobileClass("Cleric", "[B][WHITE]Cleric[R][WHITE]", 8, 2, spells, skills,
                          MobileClass.SaveType.GOOD, MobileClass.SaveType.POOR,
                          MobileClass.SaveType.GOOD, MobileClass.BaseAttackBonus.AVERAGE,
                          MobileClass.Classification.DIVINE);

    return res;
  }

  //makeDruid method.
  //Makes the Druid class.
  public static MobileClass makeDruid() {
    MobileClass res;

    //Skills.

    //Skill list.
    SkillList skills = new SkillList();

    //Spells.

    //Spell list.
    SpellList spells = new SpellList();

    //Make the class itself.
    res = new MobileClass("Druid", "[B][GREEN]Druid[R][WHITE]", 8, 4, spells, skills,
                          MobileClass.SaveType.GOOD, MobileClass.SaveType.POOR,
                          MobileClass.SaveType.GOOD, MobileClass.BaseAttackBonus.AVERAGE,
                          MobileClass.Classification.DIVINE);

    return res;
  }

  //makeRanger method.
  //Makes the Ranger class.
  public static MobileClass makeRanger() {
    MobileClass res;

    //Skills.

    //Skill list.
    SkillList skills = new SkillList();

    //Spells.

    //Spell list.
    SpellList spells = new SpellList();

    //Make the class itself.
    res = new MobileClass("Ranger", "[R][CYAN]Ranger[R][WHITE]", 8, 6, spells, skills,
                          MobileClass.SaveType.GOOD, MobileClass.SaveType.GOOD,
                          MobileClass.SaveType.POOR, MobileClass.BaseAttackBonus.GOOD,
                          MobileClass.Classification.HYBRID);

    return res;
  }

  //makeRogue method.
  //Makes the Rogue class.
  public static MobileClass makeRogue() {
    MobileClass res;

    //Skills.

    //Skill list.
    SkillList skills = new SkillList();

    //Spells.

    //Spell list.
    SpellList spells = new SpellList();

    //Make the class itself.
    res = new MobileClass("Rogue", "[R][RED]Rogue[R][WHITE]", 8, 8, spells, skills,
                          MobileClass.SaveType.POOR, MobileClass.SaveType.GOOD,
                          MobileClass.SaveType.POOR, MobileClass.BaseAttackBonus.AVERAGE,
                          MobileClass.Classification.PHYSICAL);

    return res;
  }


  //makeMonk method.
  //Makes the Monk class.
  public static MobileClass makeMonl() {
    MobileClass res;

    //Skills.

    //Skill list.
    SkillList skills = new SkillList();

    //Spells.

    //Spell list.
    SpellList spells = new SpellList();

    //Make the class itself.
    res = new MobileClass("Monk", "[R][B][BLUE]Monk[R][WHITE]", 8, 4, spells, skills,
                          MobileClass.SaveType.GOOD, MobileClass.SaveType.GOOD,
                          MobileClass.SaveType.GOOD, MobileClass.BaseAttackBonus.AVERAGE,
                          MobileClass.Classification.PHYSICAL);

    return res;
  }

  //makeWizard method.
  //Makes the Wizard class.
  public static MobileClass makeWizard() {
    MobileClass res;

    //Skills.

    //Skill list.
    SkillList skills = new SkillList();

    //Spells.

    //Spell list.
    SpellList spells = new SpellList();

    //Make the class itself.
    res = new MobileClass("Monk", "[R][B][CYAN]Wizard[R][WHITE]", 4, 2, spells,
                          skills,
                          MobileClass.SaveType.POOR, MobileClass.SaveType.POOR,
                          MobileClass.SaveType.GOOD,
                          MobileClass.BaseAttackBonus.POOR,
                          MobileClass.Classification.ARCANE);

    return res;
  }

  //makeWizard method.
  //Makes the Sorcerer class.
  public static MobileClass makeSorcerer() {
    MobileClass res;

    //Skills.

    //Skill list.
    SkillList skills = new SkillList();

    //Spells.

    //Spell list.
    SpellList spells = new SpellList();

    //Make the class itself.
    res = new MobileClass("Sorcerer", "[R][B][MAGENTA]Sorcerer[R][WHITE]", 4, 2, spells,
                          skills,
                          MobileClass.SaveType.POOR, MobileClass.SaveType.POOR,
                          MobileClass.SaveType.GOOD,
                          MobileClass.BaseAttackBonus.POOR,
                          MobileClass.Classification.ARCANE);

    return res;
  }


  //PSION CLASSES = LATER!
}
