package Game.Core;

import Game.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class LocationPosition extends Position
{
    char icon;
    boolean passable;
    private char baseIcon;
    public LocationPosition(int x, int y, boolean passable)
    {
        super(x, y);
        locationMember = null;
        locationItems = new ArrayList<>();
        this.icon = ' ';
        this.baseIcon = this.icon;
        this.passable = passable;
    }


    public LocationPosition(int x, int y, boolean passable, char icon)
    {
        super(x, y);
        locationMember = null;
        locationItems = new ArrayList<>();
        this.icon = icon;
        this.baseIcon = this.icon;
        this.passable = passable;
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

    public char getIcon()
    {
        return icon;
    }
}
