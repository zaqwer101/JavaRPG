import Game.Core.Creature;
import Game.Core.Location;
import Game.Core.Position;
import Game.Items.Item;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocationTest
{
    Location location;

    @Before
    public void before()
    {
        location = new Location(10, 10);
    }

    @Test
    public void bordersTest()
    {
        assertFalse(location.getPosition(9, 9).isPassable());
        assertFalse(location.getPosition(0, 9).isPassable());
        assertFalse(location.getPosition(9, 0).isPassable());
        assertFalse(location.getPosition(0, 9).isPassable());
        assertTrue(location.getPosition(5, 1).isPassable());

    }

    @Test
    public void iconTest()
    {
        // граница
        assertEquals('#', location.getPosition(9, 9).getIcon());

        var dummy = new Creature("Dummy", '$', new Position(2, 2), location);
        assertEquals('$', location.getPosition(2, 2).getIcon());
        try
        {
            dummy.teleport(new Position(3, 3));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();
        }
        assertEquals(' ', location.getPosition(2, 2).getIcon());
        assertEquals('$', location.getPosition(3, 3).getIcon());
    }

    @Test
    public void changeIconTest()
    {
        Item item = new Item("Item", 1, 1);
        Creature dummy = new Creature("Dummy", '$', new Position(0,0), location);
        location.getPosition(1,1).addItem(item);
        assertEquals(item.getIcon(), location.getPosition(1, 1).getIcon());

        try
        {
            dummy.teleport(new Position(1, 1));
            dummy.endTurn();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();
        }

        assertEquals(dummy.getIcon(), location.getPosition(1, 1).getIcon());

        try
        {
            dummy.teleport(new Position(0, 0));
            dummy.endTurn();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();
        }

        assertEquals(item.getIcon(), location.getPosition(1, 1).getIcon());

        location.getPosition(1,1).removeItem(item);

        assertEquals(' ', location.getPosition(1, 1).getIcon());
    }
}
