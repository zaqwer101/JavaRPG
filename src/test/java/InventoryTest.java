import Game.Core.*;
import Game.Items.Equipment.Armor.BackpackArmor;
import Game.Items.Equipment.Equipment;
import Game.Items.Item;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest
{
    BackpackArmor backpack;
    Creature dummy;
    Location location;
    Item item;

    @Before
    public void init()
    {
        int itemsInLocationCount = 10;
        location = new Location(10, 10);
        backpack = new BackpackArmor("Рюкзак", 1, 1, new Resists(), new Stats(), 50);
        dummy = new Creature("Dummy", '$', new Position(1, 1), location);
        item = new Item("KEK", 5, 20);
        for (int i = 0; i < itemsInLocationCount; i++)
            location.getPosition(dummy.getPosition().getX(), dummy.getPosition().getY()).addItem(item);
    }

    @Test
    public void locationTest()
    {
        assertEquals(item, location.getPosition(dummy.getPosition().getX(), dummy.getPosition().getY()).getItems()[0]);
    }

    @Test
    public void pickupInventoryTest()
    {
        assertEquals(item, location.getPosition(dummy.getPosition().getX(), dummy.getPosition().getY()).getItems()[0]);

        assertTrue(dummy.pickUpItem(location.getPosition(dummy.getPosition().getX(), dummy.getPosition().getY()).getItems()[0]));

        assertEquals(item, dummy.getInventory()[0]);

        assertEquals(9, location.getPosition(dummy.getPosition().getX(), dummy.getPosition().getY()).getItems().length);
    }

    @Test
    public void pickupBackpackTest()
    {
        assertEquals(item, location.getPosition(dummy.getPosition().getX(), dummy.getPosition().getY()).getItems()[0]);
        location.getPosition(dummy.getPosition()).addItem(backpack); // кладём предмет в локацию, чтобы можно было надеть оттуда
        dummy.equip(backpack);
        System.out.println(dummy.getCurrentWeight());
        assertEquals(backpack, dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK));
        assertEquals(50, dummy.getMaxFreeSize());
        assertEquals(50, ((BackpackArmor)dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).Inventory().getInventoryFreeSize());
        assertEquals(50, ((BackpackArmor)dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).Inventory().getInventoryMaxSize());
        assertEquals(0, ((BackpackArmor)dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).Inventory().getInventoryUsedSize());

        assertTrue  (dummy.pickUpItem(location.getPosition(dummy.getPosition()).getItems()[0]));
        assertEquals(5, dummy.getCurrentWeight());
        System.out.println(dummy.getCurrentWeight());
        assertTrue  (dummy.pickUpItem(location.getPosition(dummy.getPosition()).getItems()[0]));
        assertEquals(10, dummy.getCurrentWeight());
        System.out.println(dummy.getCurrentWeight());
        assertFalse(dummy.pickUpItem(location.getPosition(dummy.getPosition()).getItems()[0]));
        System.out.println(dummy.getCurrentWeight());
    }
}
