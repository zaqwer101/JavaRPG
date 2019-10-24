import Game.Core.Location;
import Game.Items.Item;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemsTest
{
    Location location;
    Item item;

    @Before
    public void init()
    {
        location = new Location(10, 10);
        item = new Item("Test", 1, 1);
    }

    @Test
    public void iconTest()
    {
        location.getPosition(5, 5).addItem(item);
        assertEquals(item, location.getPosition(5, 5).getItems()[0]);
        assertEquals(item.getIcon(), location.getPosition(5, 5).getIcon());
    }
}
