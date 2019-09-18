package Game.Items.Equipment.Armor;

import Game.Core.Resists;
import Game.Core.Stats;
import Game.Items.Equipment.Equipment;

public abstract class Armor extends Equipment
{
    Armor(String name, int weight, int size, EquipmentSlot slot, Resists resists, Stats stats)
    {
        super(name, weight, size, slot, resists, stats);
    }
}
