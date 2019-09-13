import Game.Core.Location;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(false, location.getPosition(9, 9).isPassable());
        assertEquals(false, location.getPosition(0, 9).isPassable());
        assertEquals(false, location.getPosition(9, 0).isPassable());
        assertEquals(false, location.getPosition(0, 9).isPassable());

        assertEquals(true, location.getPosition(5, 1).isPassable());
    }
}
