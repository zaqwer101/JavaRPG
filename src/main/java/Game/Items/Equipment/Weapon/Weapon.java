package Game.Items.Equipment.Weapon;

import Game.Core.Creature;
import Game.Core.JavaRPG;
import Game.Core.Resists;
import Game.Core.Stats;
import Game.Items.Equipment.Equipment;

public class Weapon extends Equipment
{
    public Weapon(String name, int weight, int size, EquipmentSlot slot, Resists resists, Stats stats) {
        super(name, weight, size, slot, resists, stats);
    }

    @Override
    public void onEquip(Creature target)
    {
        target.addStats(getStats());
        target.addResists(getResists());

        JavaRPG.log(target.getName() + ": надел " + getName());
    }
}
