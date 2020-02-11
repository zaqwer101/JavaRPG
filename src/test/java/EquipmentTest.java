import Game.Core.*;
import Game.Effects.InstantDamage;
import Game.Items.Equipment.Armor.BodyArmor;
import Game.Items.Equipment.Equipment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EquipmentTest
{
    Stats equipmentStats, equipmentRequirements;
    Equipment equipment;
    Creature dummy;
    Location location;

    @Before
    public void init()
    {
        location = new Location(10, 10);
        equipmentRequirements = new Stats();
        equipmentStats = new Stats();
        equipmentStats.setStat("strength",      10);
        equipmentStats.setStat("agility",       10);
        equipmentStats.setStat("intelligence",  10);
        dummy = new Creature("Dummy", '$', new Position(0, 0), location);
    }

    @Test
    public void armorStatsTest()
    {
        equipment = new BodyArmor("Доспех", 1, 5, new Resists(), equipmentStats, equipmentRequirements);
        assertEquals(1, dummy.getStat("strength"));
        assertEquals(1, dummy.getStat("agility"));
        assertEquals(1, dummy.getStat("intelligence"));
        location.getPosition(dummy.getPosition()).addItem(equipment); // кладём предмет в локацию, чтобы можно было надеть оттуда

        dummy.equip(equipment);
        assertEquals(11, dummy.getStat("strength"));
        assertEquals(11, dummy.getStat("agility"));
        assertEquals(11, dummy.getStat("intelligence"));
        assertEquals(equipment, dummy.getEquipment(equipment.getSlot()));

        dummy.unEquip(equipment);

        assertEquals(1, dummy.getStat("strength"));
        assertEquals(1, dummy.getStat("agility"));
        assertEquals(1, dummy.getStat("intelligence"));

        assertEquals(equipment, dummy.getInventory()[0]);
        assertNull  (dummy.getEquipment(equipment.getSlot()));

    }

    @Test
    public void armorResistsTest()
    {
        Resists resists = new Resists();
        resists.setResist(Resists.DamageType.PHYSICAL, 90);
        equipment = new BodyArmor("Доспех", 10, 1, resists, equipmentStats, equipmentRequirements);
        location.getPosition(dummy.getPosition()).addItem(equipment); // кладём предмет в локацию, чтобы можно было надеть оттуда
        // Creature tester = new Creature("Dummy", '!', new Position(1, 1), location);

        assertEquals(8, dummy.getHp()[0]);

        dummy.equip(equipment);
        new InstantDamage("ХУЯК", Resists.DamageType.PHYSICAL, 10).apply(dummy);

        assertEquals(7, dummy.getHp()[0]);
    }
}
