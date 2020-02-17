import Game.Core.*;
import Game.Effects.InstantDamage;
import Game.Effects.PeriodicStatsEffect;
import Game.Items.Equipment.Armor.BodyArmor;
import Game.Items.Equipment.Equipment;
import Game.Items.Equipment.Weapon.ShortSword;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EquipmentTest
{
    Stats equipmentStats, equipmentRequirements;
    Equipment equipment1, equipment2;
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
        equipment1 = new BodyArmor("Доспех", 1, 5, new Resists(), equipmentStats, equipmentRequirements);
        assertEquals(1, dummy.getStat("strength"));
        assertEquals(1, dummy.getStat("agility"));
        assertEquals(1, dummy.getStat("intelligence"));
        location.getPosition(dummy.getPosition()).addItem(equipment1); // кладём предмет в локацию, чтобы можно было надеть оттуда

        dummy.equip(equipment1);
        assertEquals(11, dummy.getStat("strength"));
        assertEquals(11, dummy.getStat("agility"));
        assertEquals(11, dummy.getStat("intelligence"));
        assertEquals(equipment1, dummy.getEquipment(equipment1.getSlot()));

        dummy.unEquip(equipment1);

        assertEquals(1, dummy.getStat("strength"));
        assertEquals(1, dummy.getStat("agility"));
        assertEquals(1, dummy.getStat("intelligence"));

        assertEquals(equipment1, dummy.getInventory()[0]);
        assertNull  (dummy.getEquipment(equipment1.getSlot()));

    }

    @Test
    public void armorResistsTest()
    {
        Resists resists = new Resists();
        resists.setResist(Resists.DamageType.PHYSICAL, 90);
        equipment1 = new BodyArmor("Доспех", 10, 1, resists, equipmentStats, equipmentRequirements);
        location.getPosition(dummy.getPosition()).addItem(equipment1); // кладём предмет в локацию, чтобы можно было надеть оттуда

        assertEquals(8, dummy.getHp()[0]);

        dummy.equip(equipment1);
        new InstantDamage("ХУЯК", Resists.DamageType.PHYSICAL, 10).apply(dummy);

        assertEquals(7, dummy.getHp()[0]);
    }

    @Test
    public void equipmentRequirementsTest()
    {
        equipmentRequirements.setStat("strength", 100);
        equipmentRequirements.setStat("intelligence", 100);
        equipment1 = new BodyArmor("Доспех", 10, 1, null, null, equipmentRequirements);
        dummy.addToInventory(equipment1);
        assertFalse(dummy.equip(equipment1));
        assertNull(dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BODY));

        Stats buffStats = new Stats();
        buffStats.setStat("strength", 110);
        buffStats.setStat("intelligence", 110);
        dummy.addEffect(new PeriodicStatsEffect(1, "Чтоб броня не спадала", buffStats));
        dummy.endTurn();

        assertTrue(dummy.equip(equipment1));
        assertEquals(equipment1, dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BODY));

        assertEquals(1, dummy.getEffects()[0].getDuration());
        dummy.endTurn();
        assertEquals(0, dummy.getEffects().length);

        assertEquals(1, dummy.getStat(  "strength"      ));
        assertEquals(1, dummy.getStat(  "intelligence"  ));

        assertNull(dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BODY)); // Броня должна упасть

    }

    @Test
    public void equipTest()
    {
        equipment1 = new ShortSword("Меч 1", 1, 1, null, null, null, 10);
        equipment2 = new ShortSword("Меч 2", 1, 1, null, null, null, 10);
        Equipment equipment3 = new ShortSword("Меч 3", 1, 1, null, null, null, 10);

        dummy.addToInventory(equipment1);
        dummy.addToInventory(equipment2);
        dummy.addToInventory(equipment3);

        // в две руки по одноручному
        assertTrue(dummy.equip(equipment1));
        assertEquals(2, dummy.getInventory().length);
        assertTrue(dummy.equip(equipment2));
        assertEquals(1, dummy.getInventory().length);

        // третье надеть нельзя
        assertFalse(dummy.equip(equipment3));

        assertEquals(1, dummy.getInventory().length);
        dummy.deleteFromInventory(equipment3);

        equipment1 = new BodyArmor("Броня 1", 1, 1, null, null, null);
        equipment2 = new BodyArmor("Броня 2", 1, 1, null, null, null);
        location.getPosition(dummy.getPosition()).addItem(equipment1);
        dummy.addToInventory(equipment2);

        assertTrue(dummy.equip(equipment1));
        assertFalse(dummy.equip(equipment2));
        assertEquals(dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BODY), equipment1);
    }

    @Test
    public void unEquipTest()
    {
        equipment1 = new BodyArmor("Armor", 1, 1, null, null, null);
        dummy.addToInventory(equipment1);
        assertTrue(dummy.equip(equipment1));

        dummy.unEquip(equipment1);
        assertNull(dummy.getEquipment(equipment1.getSlot()));
        assertNull(dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BODY));
    }

}
