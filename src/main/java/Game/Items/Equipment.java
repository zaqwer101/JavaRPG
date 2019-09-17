package Game.Items;

import Game.Core.Creature;
import Game.Core.Resists;
import Game.Core.Stats;

public class Equipment extends Item
{
    public static enum EquipmentSlot {  EQUIPMENT_HEAD, EQUIPMENT_NECK, EQUIPMENT_BODY,
                                        EQUIPMENT_HANDS, EQUIPMENT_LEGS };

    private EquipmentSlot slot;
    private Resists resists;
    private Stats  stats;

    public Equipment(String name, int weight, int size)
    {
        super(name, weight, size);
    }

    public void onEquip(Creature target)
    {
        target.addStats(stats);
        target.addResists(resists);
    }
}
