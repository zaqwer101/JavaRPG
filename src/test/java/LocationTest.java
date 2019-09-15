import Game.Core.Location;
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
    public void borders()
    {
        assertFalse(location.getPosition(9, 9).isPassable());
        assertFalse(location.getPosition(0, 9).isPassable());
        assertFalse(location.getPosition(9, 0).isPassable());
        assertFalse(location.getPosition(0, 9).isPassable());
        assertTrue(location.getPosition(5, 1).isPassable());
    }
}
