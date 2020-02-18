package Game.Items.Equipment.Weapon;

import Game.Attacks.Attack;
import Game.Core.Creature;
import Game.Core.JavaRPG;
import Game.Core.Resists;
import Game.Core.Stats;
import Game.Items.Equipment.Equipment;

import java.util.ArrayList;

public abstract class Weapon extends Equipment
{
    protected ArrayList<Attack> attacks;
    protected boolean isTwoHanded;

    public Weapon(String name, int weight, int size, Resists resists, Stats stats, Stats requirements, ArrayList<Attack> attacks) {
        super(name, weight, size, EquipmentSlot.EQUIPMENT_RIGHTHAND, resists, stats, requirements);
        this.attacks = attacks;
    }

    @Override
    public void onEquip(Creature target)
    {
        if(getStats() != null)
            target.addStats(getStats());
        if (getResists() != null)
            target.addResists(getResists());
        target.recountAttacks();
        JavaRPG.log(target.getName() + ": надел " + getName());
    }

    public ArrayList<Attack> getAttacks()
    {
        return attacks;
    }

    public boolean isTwoHanded()
    {
        return isTwoHanded;
    }
}