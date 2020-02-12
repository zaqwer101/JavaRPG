import Game.Core.*;
import Game.Effects.InstantDamage;
import Game.Effects.PeriodicStatsEffect;
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

        assertEquals(8, dummy.getHp()[0]);

        dummy.equip(equipment);
        new InstantDamage("ХУЯК", Resists.DamageType.PHYSICAL, 10).apply(dummy);

        assertEquals(7, dummy.getHp()[0]);
    }

    @Test
    public void equipmentRequirementsTest()
    {
        equipmentRequirements.setStat("strength", 100);
        equipmentRequirements.setStat("intelligence", 100);
        equipment = new BodyArmor("Доспех", 10, 1, null, null, equipmentRequirements);
        dummy.addToInventory(equipment);
        assertFalse(dummy.equip(equipment));
        assertNull(dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BODY));

        Stats buffStats = new Stats();
        buffStats.setStat("strength", 110);
        buffStats.setStat("intelligence", 110);
        dummy.addEffect(new PeriodicStatsEffect(1, "Чтоб броня не спадала", buffStats));
        dummy.endTurn();

        assertTrue(dummy.equip(equipment));
        assertEquals(equipment, dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BODY));

        assertEquals(1, dummy.getEffects()[0].getDuration());
        dummy.endTurn();
        assertEquals(0, dummy.getEffects().length);

        assertEquals(1, dummy.getStat(  "strength"      ));
        assertEquals(1, dummy.getStat(  "intelligence"  ));

        assertNull(dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BODY)); // Броня должна упасть

    }
}
