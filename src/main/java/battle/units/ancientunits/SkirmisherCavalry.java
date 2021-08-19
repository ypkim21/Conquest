package battle.units.ancientunits;

import battle.interfaces.Unit;
import testing.unittesting.StateType;

import java.util.Random;

public class SkirmisherCavalry implements Unit {
    /** name of the skirmisher cavalry unit (i.e. skirmisher cavalry, horse archers). */
    private String name;
    /** number of soldiers in skirmisher cavalry unit. */
    private int number;
    /** maximum possible number of soldiers in the unit. */
    private int limit;
    /** cost of buying this new unit. */
    private int unitCost;

    /** amount of melee damage the unit deals */
    private int meleeDamage;
    /** maximum amount of ranged damage the unit deals. */
    private int rangedDamage;
    /** amount of armor the unit has (lowers damage inflicted by a constant
     *  amount). */
    private int armor;
    /** movement cost for moving one space on the battle board. */
    private int movementCost;

    /** number of diagonal squares that make up range of unit. */
    private int rangeFactor;
    /** how much defense the special ability provides, as
     *  a percentage of how much attack damage is reduced. */
    private double movementBonus;
    /** boolean checking whether or not the unit's special ability
     * is activated. */
    private boolean isActivated;

    public SkirmisherCavalry() {
        this.name = null;
        this.limit = 0;
        this.number = 0;
        this.unitCost = 0;
        this.meleeDamage = 0;
        this.rangedDamage = 0;
        this.armor = 0;
        this.movementCost = 0;
        this.rangeFactor = 0;
        this.movementBonus = 0;
    }

    public SkirmisherCavalry(final String name, final int limit, final int unitCost,
                 final int meleeDamage, final int rangedDamage, final int armor,
                 final int movementCost, final int rangeFactor,
                 final double movementBonus) {
        this.name = name;
        this.limit = limit;
        this.number = limit;
        this.unitCost = unitCost;
        this.meleeDamage = meleeDamage;
        this.rangedDamage = rangedDamage;
        this.armor = armor;
        this.movementCost = movementCost;
        this.rangeFactor = rangeFactor;
        this.movementBonus = movementBonus;
    }

    public String getUnitName() {
        return this.name;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getUnitCost() {
        return this.unitCost;
    }

    public int getArmor() {
        return this.armor;
    }

    public int getMeleeDamage() {
        return this.meleeDamage;
    }

    public int getRangedDamage() {
        return this.rangedDamage;
    }

    public int getMovementCost() {
        // reduces movement cost to certain percentage (movementBonus),
        // allowing skirmisher cavalry unit to travel more distance
        if (this.isActivated) {
            return (int) this.movementBonus * this.movementCost;
        }

        return this.movementCost;
    }

    public int getRangeFactor() {
        return this.rangeFactor;
    }

    public String getUnitSummary() {
        StringBuilder s = new StringBuilder();
        s.append(this.name);
        s.append("\n");
        s.append(this.number);
        s.append(" \\ ");
        s.append(this.limit);
        s.append("\n");
        s.append("Melee Damage: ");
        s.append(this.meleeDamage);
        s.append("\nRanged Damage: ");
        s.append(this.rangedDamage);
        s.append("\nArmor: ");
        s.append(this.armor);
        if (this.isActivated) {
            s.append("\nAbility active");
        } else {
            s.append("\nAbility inactive");
        }
        return s.toString();
    }

    public StateType damageUnit(int damage) {
        // checks whether parameter is invalid
        if (damage < 0) {
            return StateType.returnFailure("Error: damage parameter invalid");
        }

        int finalDamage = damage - this.armor;

        // if armor is sufficient enough to block attack
        // completely, then the unit is undamaged
        if (finalDamage <= 0) {
            return StateType.returnSuccess(null);
        }

        // subtracts unit number by the final damage inflicted
        this.number -= finalDamage;

        if (this.number <= 0) {
            return StateType.returnDestroy("Note: Unit has been destroyed");
        }

        // method success
        return StateType.returnSuccess(null);
    }

    public StateType attackWithRange(Unit unit, double areaBonus) {
        // validating parameters
        if (unit == null) {
            return StateType.returnFailure("Error: null unit param");
        }

        if (areaBonus < 0 || areaBonus > 1) {
            return StateType.returnFailure("Error: invalid area param");
        }

        // calculates damage inflicted on enemy
        // based on range of possible damages and Java's Random type
        Random random = new Random();
        double damage = random.nextInt(this.rangedDamage);

        // applies area bonus of unit to reduce damage
        damage *= areaBonus;

        // inflicts damage onto enemy unit
        return unit.damageUnit((int) damage);
    }

    public StateType attackWithMelee(Unit unit, double areaBonus) {
        // validating parameters
        if (unit == null) {
            return StateType.returnFailure("Error: null unit param");
        }

        if (areaBonus < 0 || areaBonus > 1) {
            return StateType.returnFailure("Error: invalid area param");
        }

        // computes the damage after taking into account terrain
        // defenses
        double damage = this.meleeDamage * areaBonus;

        // lowers the enemy unit by a certain amount of damage or
        // more, depending on if other unit has defense bonus
        return unit.damageUnit((int) damage);
    }

    public void activateSpecialAbility() {
        this.isActivated = true;
    }

    public void deactivateSpecialAbility() {
        this.isActivated = false;
    }

    public boolean specialIsActivated() {
        return this.isActivated;
    }

    public boolean isRangedUnit() {
        return true;
    }

    public void refill() {
        this.number = this.limit;
    }

    public UnitType getUnitType() {
        return UnitType.CAVALRY;
    }

    public Unit copyUnit() {
        SkirmisherCavalry copy = new SkirmisherCavalry();
        copy.name = this.name;
        copy.number = this.number;
        copy.limit = this.limit;
        copy.unitCost = this.unitCost;
        copy.meleeDamage = this.meleeDamage;
        copy.rangedDamage = this.rangedDamage;
        copy.armor = this.armor;
        copy.movementCost = this.movementCost;
        copy.rangeFactor = this.rangeFactor;
        copy.movementBonus = this.movementBonus;

        return copy;
    }
}
