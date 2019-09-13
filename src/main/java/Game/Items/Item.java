package Game.Items;

import Game.Core.WorldObject;

public class Item extends WorldObject
{
    private int weight, size;

    public Item(String name, int weight, int size)
    {
        super(name, '\"');
        this.size = size;
        this.weight = weight;
    }

    public int getWeight()
    {
        return weight;
    }

    public int getSize()
    {
        return size;
    }
}
