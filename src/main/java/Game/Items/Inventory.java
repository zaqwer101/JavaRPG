package Game.Items;

import java.util.ArrayList;

public class Inventory
{
    int size;
    private ArrayList<Item> inventory;

    public Inventory(int size)
    {
        inventory = new ArrayList<>();
        this.size = size;
    }

    public int getInventoryMaxSize()
    {
        return size;
    }

    public int getInventoryUsedSize()
    {
        int used = 0;
        for (var item : inventory)
        {
            used += item.getSize();
        }
        return used;
    }

    public int getInventoryFreeSize()
    {
        return getInventoryMaxSize() - getInventoryUsedSize();
    }

    public boolean addItem(Item item)
    {
        if (getInventoryFreeSize() >= item.getSize())
        {
            inventory.add(item);
            return  true;
        }
        return false;
    }

    public boolean deleteItem(Item item)
    {
        if (inventory.contains(item))
        {
            inventory.remove(item);
            return true;
        }
        return false;
    }

    public ArrayList<Item> getInventory()
    {
        return inventory;
    }
}