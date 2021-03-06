package Game.Core;

import java.util.ArrayList;

public class Location
{
    ArrayList<LocationPosition> location;
    private int sizeX, sizeY;
    public Location(int sizeX, int sizeY)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        location = new ArrayList<>();
        for (int i = 0; i < sizeY; i++)
        {
            for (int f = 0; f < sizeX; f++)
            {
                boolean passable = true;
                if (i == 0 || f == 0 || f == sizeX - 1 || i == sizeY - 1)
                    location.add(new LocationPosition(f, i, false, '#'));
                else
                    location.add(new LocationPosition(f, i, true, 1));
            }
        }
    }

    public LocationPosition getPosition(int x, int y)
    {
        for (var l : location)
        {
            if(l.getX() == x && l.getY() == y)
            {
                return l;
            }
        }
        return null;
    }

    public LocationPosition getPosition(Position position)
    {
        for (var l : location)
        {
            if(l.getX() == position.getX() && l.getY() == position.getY())
            {
                return l;
            }
        }
        return null;
    }

    public int[] getSize()
    {
        return new int[] {sizeX, sizeY};
    }

    public ArrayList<LocationPosition> getLocation()
    {
        return location;
    }
}
