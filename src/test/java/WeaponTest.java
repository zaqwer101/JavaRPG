import Game.Actions.AttackAction;
import Game.Core.*;
import Game.Effects.PeriodicStatsEffect;
import Game.Items.Equipment.Equipment;
import Game.Items.Equipment.Weapon.Swords.LongSword;
import Game.Items.Equipment.Weapon.Swords.ShortSword;
import Game.Items.Equipment.Weapon.Weapon;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeaponTest
{
    Location location;
    Creature dummy, target;
    Weapon weapon;
    Stats weaponRequirements;

    @Before
    public void before()
    {
        weaponRequirements = new Stats();
        weapon = new ShortSword("Sword", 1, 1, null, null, weaponRequirements, 10);
        location = new Location(10, 10);
        dummy = new Creature("Dummy", '$', new Position(2, 2), location);
        target = new Creature("Target", 'T', new Position(3, 3), location);
        dummy.getLocation().getPosition(dummy.getPosition()).addItem(weapon);
    }

    @Test
    public void weaponEquipTest()
    {
        JavaRPG.log(dummy.getAttacks()[0].getName());
        assertEquals(weapon, location.getPosition(dummy.getPosition()).getItems()[0]);
        dummy.pickUpItem(weapon);
        assertEquals(0, location.getPosition(dummy.getPosition()).getItems().length);
        assertEquals(weapon, dummy.getInventory()[0]);

        dummy.equip(weapon);
        assertEquals(0, dummy.getInventory().length);
        assertEquals(weapon, dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND));
        assertEquals(1, dummy.getAttacks().length);
        JavaRPG.log(dummy.getAttacks()[0].getName());
        assertEquals("Атака мечом", dummy.getAttacks()[0].getName());

        dummy.unEquip(dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND));
        assertEquals(null, dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND));
        assertEquals(weapon, dummy.getInventory()[0]);
    }

    @Test
    public void weaponAttackTest()
    {
        var additionalStats = new Stats();
        additionalStats.setStat("strength", 100);
        target.addEffect(new PeriodicStatsEffect(10, "Чтоб не сдох", additionalStats));
        target.endTurn();
        target.heal(10000);
        assertEquals(target.getHp()[1], target.getHp()[0]); // смотрим, что полные хп
        JavaRPG.log("Сейчас здоровья: " + target.getHp()[0]);

        dummy.pickUpItem(weapon);
        assertTrue(dummy.equip(weapon));
        assertEquals(weapon, dummy.getWeapons()[0]);
        dummy.performAction(new AttackAction(dummy, target, 0, dummy.getAttacks()[0]));

        assertEquals(target.getHp()[1] - ((Weapon)(dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND))).getAttacks().get(0).getDamage(),
                target.getHp()[0]); // проверяем, что удар достиг цели
    }

    @Test
    public void twoHandedWeaponEquipTest()
    {
        weapon = new LongSword("Long sword", 1, 1, null, null, null, 10);
        dummy.addToInventory(weapon);
        assertTrue(dummy.equip(weapon));

        var weapon2 = new LongSword("Long sword", 1, 1, null, null, null, 10);
        dummy.addToInventory(weapon2);
        assertFalse(dummy.equip(weapon2));
        dummy.unEquip(weapon);
        dummy.deleteFromInventory(weapon);
        dummy.deleteFromInventory(weapon2);

        weapon = new ShortSword("Short sword", 1, 1, null, null, null, 10);
        dummy.addToInventory(weapon2);
        dummy.addToInventory(weapon);

        assertTrue(dummy.equip(weapon));
        assertFalse(dummy.equip(weapon2));
        dummy.unEquip(weapon);

        assertTrue(dummy.equip(weapon2));
        assertFalse(dummy.equip(weapon));
    }
}
