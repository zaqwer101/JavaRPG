import Game.Actions.AttackAction;
import Game.Core.*;
import Game.Effects.InstantDamage;
import Game.Effects.PeriodicStatsEffect;
import Game.Items.Equipment.Armor.BodyArmor;
import Game.Items.Equipment.Equipment;
import Game.Items.Equipment.Weapon.ShortSword;
import Game.Items.Equipment.Weapon.Weapon;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeaponTest
{
    Location location;
    Creature dummy, target;
    Weapon weapon;

    @Before
    public void before()
    {
        location = new Location(10, 10);
        dummy = new Creature("Dummy", '$', new Position(2, 2), location);
        target = new Creature("Target", 'T', new Position(3, 3), location);
        weapon = new ShortSword("Sword", 1, 1, Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND, null, null, 10);

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
        dummy.equip(weapon);
        dummy.performAction(new AttackAction(dummy, target, 0, dummy.getAttacks()[0]));

        assertEquals(target.getHp()[1] - ((Weapon)(dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND))).getAttacks().get(0).getDamage(),
                target.getHp()[0]); // проверяем, что удар достиг цели
    }
}
