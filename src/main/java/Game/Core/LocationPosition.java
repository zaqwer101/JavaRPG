package Game.Core;

import Game.Items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Точка игровой локации
 */
public class LocationPosition extends Position
{
    public static char ICON_EMPTY = ' ';
    public enum Layers {LAYER_LANDSCAPE, LAYER_ITEM, LAYER_CREATURE};
    char icon;
    boolean passable;
    private char baseIcon;
    private ArrayList<Item> items;
    private int movementCost;
    HashMap<Layers, Character> layers;

    public LocationPosition(int x, int y, boolean passable, int movementCost)
    {
        super(x, y);
        layers = new HashMap<>();
        for (var layer : Layers.values())
        {
            layers.put(layer, ICON_EMPTY);
        }

        this.movementCost = movementCost;
        locationMember = null;
        locationItems = new ArrayList<>();
        this.icon = ICON_EMPTY;
        this.baseIcon = this.icon;
        this.passable = passable;
        items = new ArrayList<>();
    }


    public LocationPosition(int x, int y, boolean passable, char icon)
    {
        super(x, y);
        layers = new HashMap<>();
        for (var layer : Layers.values())
        {
            layers.put(layer, ICON_EMPTY);
        }

        locationMember = null;
        locationItems = new ArrayList<>();
        this.icon = icon;
        this.baseIcon = this.icon;
        this.passable = passable;
        items = new ArrayList<>();
        layers.replace(Layers.LAYER_LANDSCAPE, baseIcon);
    }

    private Creature locationMember;
    private ArrayList<Item> locationItems;

    public boolean isPassable()
    {
        return passable;
    }

    public boolean isEmpty()
    {
        return locationMember == null;
    }

    public void setMember(Creature member)
    {
        this.locationMember = member;
        if(member != null)
            this.icon = member.getIcon();
        else
            this.icon = baseIcon;
    }

    public Creature getMember()
    {
        return locationMember;
    }

    private void setIcon(char icon)
    {
        this.icon = icon;
    }

    /**
     * Возвращает список возможных иконок позиции по слоям
     * @return
     */
    public char[] getIcons()
    {
        layers.replace(Layers.LAYER_LANDSCAPE, baseIcon); // TODO, ландшафт сейчас не используется никак

        if (locationMember != null)
        {
            layers.replace(Layers.LAYER_CREATURE, locationMember.getIcon());
        }
        else
            layers.replace(Layers.LAYER_CREATURE, ICON_EMPTY);

        if (items.size() > 0)
        {
            layers.replace(Layers.LAYER_ITEM, items.get(items.size() - 1).getIcon());
        }
        else
            layers.replace(Layers.LAYER_ITEM, ICON_EMPTY);

        return new char[] {
                layers.get(Layers.LAYER_CREATURE),
                layers.get(Layers.LAYER_ITEM),
                layers.get(Layers.LAYER_LANDSCAPE)
        };
    }

    /**
     * Возвращает иконку самого верхнего слоя
     * @return
     */
    public char getIcon()
    {
        var icons = getIcons();
        for (int i = 0; i < icons.length; i++)
        {
            if (icons[i] != ICON_EMPTY)
                return icons[i];
        }
        return ICON_EMPTY;
    }

    public void addItem(Item item)
    {
        items.add(item);
    }

    public Item[] getItems()
    {
        return items.toArray(new Item[0]);
    }
    public void removeItem(Item item)
    {
        items.remove(item);
    }
    public int getMovementCost()
    {
        return movementCost;
    }
}
