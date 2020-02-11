package Game.Items.Equipment;

import Game.Core.Creature;
import Game.Core.JavaRPG;
import Game.Core.Resists;
import Game.Core.Stats;
import Game.Items.Item;

import java.util.Arrays;

public class Equipment extends Item
{
    public static enum EquipmentSlot
    {
        EQUIPMENT_HEAD, EQUIPMENT_NECK, EQUIPMENT_BODY, EQUIPMENT_HANDS, EQUIPMENT_LEGS, EQUIPMENT_BACKPACK,
        EQUIPMENT_RIGHTHAND, EQUIPMENT_LEFTHAND
    }

    private EquipmentSlot slot;
    private Resists resists;
    private Stats stats, requirements;

    public Equipment(String name, int weight, int size, EquipmentSlot slot, Resists resists, Stats stats, Stats requirements)
    {
        super(name, weight, size);
        this.resists = resists;
        this.slot = slot;
        this.stats = stats;
        this.requirements = requirements;
    }

    public void onEquip(Creature target)
    {
        if(stats != null)
            target.addStats(stats);
        if(resists != null)
            target.addResists(resists);
        JavaRPG.log(target.getName() + ": надел " + getName());
    }

    public void onUnEquip(Creature target)
    {
        if(stats != null)
            target.subStats(stats);
        if(resists != null)
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

    public boolean checkRequirements(Creature target)
    {
        for (var stat : Stats.allStats)
        {
            if (!Arrays.asList(Stats.nonFoldingStats).contains(stat))
            {
                if (requirements.getStat(stat) > target.getStat(stat))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
