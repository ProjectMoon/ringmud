package ring.mobiles;

import ring.effects.Affectable;

/**
 * Represents the combat model of a mobile in the game: base attack bonus,
 * hp, armor class, etc. These values can change frequently due to Effects
 * and various other things.
 *
 * @author projectmoon
 */
public class MobileCombatModel {
    // The maximum HP of the mobile. We don't need a maximum mana because the
    // MUD has no mana system. Spells are prepared and do not use mana.
    private int maxHP;

    public int getMaxBaseHP() {
        return maxHP;
    }

    public void setMaxBaseHP(int hp) {
        maxHP = hp;
    }

    public int getMaxHP() {
        return maxHP + bonusHP;
    }

    public String getMaxHPString() {
        return String.valueOf(getMaxHP());
    }

    // The current HP of the mobile. If it goes below -10, the mobile's isDead
    // boolean value is set to true. This, of course, will kill the mobile.
    private int currentHP;

    public int getCurrentHP() {
        return currentHP + bonusHP;
    }

    public void setCurrentHP(int hp) {
        currentHP = hp;
    }

    public void changeCurrentHP(int amount) {
        currentHP += amount;
    }

    public String getCurrentHPString() {
        return String.valueOf(getCurrentHP());
    }

    public int getCurrentBaseHP() {
        return currentHP;
    }

    public void setCurrentBaseHP(int hp) {
        currentHP = hp;
    }

    // The HP bonus of the mobile. This number is added on to the maxHP variable
    // when HP is being displayed. It is also added on to the currentHP variable.
    // This number is a conglomeration of HP bonuses and penalties from various
    // sources.
    private int bonusHP;

    public int getBonusHP() {
        return bonusHP;
    }

    public void changeBonusHP(int amount) {
        bonusHP += amount;
    }

    // The generic attack bonus of the mobile. This number is a composite of all
    // modifiers applied to the mobiles attacks, bonuses and penalties.
    private int attackBonus;

    public int getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(int bonus) {
        attackBonus = bonus;
    }

    public void changeAttackBonus(int amount) {
        attackBonus += amount;
    }

    // Armor class of the mobile. This determines how hard the mobile is to hit.
    private int currentAC;

    public int getCurrentAC() {
        return currentAC;
    }

    public void setCurrentAC(int ac) {
        currentAC = ac;
    }

    public void changeCurrentAC(int amount) {
        currentAC += amount;
    }

    // Current target of the mobile. This will be used as an automatic parameter
    // during combat when using hostile
    // actions in combat if no parameter is actively specified. It is also the
    // mobile's current auto-attack target.
    private Affectable target;

    public Affectable getTarget() {
        return target;
    }

    public void setTarget(Affectable target) {
        this.target = target;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + attackBonus;
        result = prime * result + bonusHP;
        result = prime * result + currentAC;
        result = prime * result + currentHP;
        result = prime * result + maxHP;
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MobileCombatModel other = (MobileCombatModel) obj;
        if (attackBonus != other.attackBonus)
            return false;
        if (bonusHP != other.bonusHP)
            return false;
        if (currentAC != other.currentAC)
            return false;
        if (currentHP != other.currentHP)
            return false;
        if (maxHP != other.maxHP)
            return false;
        if (target == null) {
            if (other.target != null)
                return false;
        } else if (!target.equals(other.target))
            return false;
        return true;
    }
}
