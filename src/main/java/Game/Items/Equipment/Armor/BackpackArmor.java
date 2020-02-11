package Game.Items.Equipment.Armor;

import Game.Core.Resists;
import Game.Core.Stats;
import Game.Items.Inventory;
import Game.Items.Item;

public class BackpackArmor extends Armor
{
    Inventory inventory;
    public BackpackArmor(String name, int weight, int size, Resists resists, Stats stats, Stats requirements, int inventorySize)
    {
        super(name, weight, size, EquipmentSlot.EQUIPMENT_BACKPACK, resists, stats, requirements);
        inventory = new Inventory(inventorySize);
    }

    public Inventory Inventory()
    {
        return inventory;
    }

    public boolean addToInventory(Item item)
    {
        return inventory.addItem(item);
    }
}
