package Game.Core;

import Game.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class LocationPosition extends Position
{
    char icon;
    boolean passable;
    public LocationPosition(int x, int y, boolean passable)
    {
        super(x, y);
        locationMember = null;
        locationItems = new ArrayList<>();
        this.icon = ' ';
        this.passable = passable;
    }


    public LocationPosition(int x, int y, boolean passable, char icon)
    {
        super(x, y);
        locationMember = null;
        locationItems = new ArrayList<>();
        this.icon = icon;
        this.passable = passable;
    }

    private Creature locationMember;
    private ArrayList<Item> locationItems;

    public boolean isPassable()
    {
        return passable;
    }

    private void setIcon(char icon)
    {
        this.icon = icon;
    }
}
