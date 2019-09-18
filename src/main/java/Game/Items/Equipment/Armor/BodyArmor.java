package Game.Items.Equipment.Armor;

import Game.Core.Resists;
import Game.Core.Stats;

public class BodyArmor extends Armor
{
    public BodyArmor(String name, int weight, int size, Resists resists, Stats stats)
    {
        super(name, weight, size, EquipmentSlot.EQUIPMENT_BODY, resists, stats);
    }
}
