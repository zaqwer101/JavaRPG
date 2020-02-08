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
    private ArrayList<Attack> attacks;
    private boolean isTwoHanded;

    public Weapon(String name, int weight, int size, EquipmentSlot slot, Resists resists, Stats stats, ArrayList<Attack> attacks) {
        super(name, weight, size, slot, resists, stats);
        this.attacks = attacks;
    }

    // TODO
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