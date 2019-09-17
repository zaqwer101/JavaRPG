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

    public Equipment(String name, int weight, int size, EquipmentSlot slot, Resists resists, Stats stats)
    {
        super(name, weight, size);
        this.resists = resists;
        this.slot = slot;
        this.stats = stats;
    }

    public void onEquip(Creature target)
    {
        target.addStats(stats);
        target.addResists(resists);
    }

    public void onUnEquip(Creature target)
    {
        target.subStats(stats);
        target.subResists(resists);
    }

    public EquipmentSlot getSlot()
    {
        return slot;
    }

    public Stats getStats()
    {
        return stats;
    }

    public Resists getResists()
    {
        return resists;
    }
}
