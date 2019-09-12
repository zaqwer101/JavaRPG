package Game.Core;

import Game.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class LocationPosition extends Position
{
    char icon;
    public LocationPosition(int x, int y)
    {
        super(x, y);
        locationMember = null;
        locationItems = new ArrayList();
    }

    private Creature locationMember;
    private ArrayList<Item> locationItems;
}
