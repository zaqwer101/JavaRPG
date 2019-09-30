package Game.Items.Equipment;

import Game.Core.Creature;
import Engine.JavaRPG;
import Game.Core.Resists;
import Game.Core.Stats;
import Game.Items.Item;

public class Equipment extends Item
{
    public static enum EquipmentSlot    {   EQUIPMENT_HEAD, EQUIPMENT_NECK, EQUIPMENT_BODY,
                                            EQUIPMENT_HANDS, EQUIPMENT_LEGS,
                                            EQUIPMENT_BACKPACK
                                        };

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
        JavaRPG.log(target.getName() + ": надел " + getName());
    }

    public void onUnEquip(Creature target)
    {
        target.subStats(stats);
        target.subResists(resists);
        JavaRPG.log(target.getName() + ": снял " + getName());
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
