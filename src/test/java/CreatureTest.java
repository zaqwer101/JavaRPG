import Game.Actions.AttackAction;
import Game.Actions.EquipAction;
import Game.Actions.MoveAction;
import Game.Actions.PickupAction;
import Game.Attacks.Attack;
import Game.Attacks.MeleeAttack;
import Game.Attacks.VampireBite;
import Game.Core.*;
import Game.Effects.PeriodicDamageEffect;
import Game.Effects.InstantHeal;
import Game.Effects.PeriodicStatsEffect;
import Game.Items.Equipment.Armor.BodyArmor;
import Game.Items.Equipment.Equipment;
import Game.Items.Item;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CreatureTest {
    Creature dummy;
    Creature target;
    Location location;
    @Before
    public void init()
    {
        location = new Location(10, 10);
        dummy = new Creature("Dummy", '$', new Position(0, 0), location);
        target = new Creature("Target", 'T', new Position(1, 1), location);
    }

    @Test
    public void takeDamageTest()
    {
        dummy.takeDamage(1, Resists.DamageType.PHYSICAL);
        assertEquals(7, dummy.getHp()[0]);
    }

    @Test
    public void damageEffectTest()
    {
        int steps = 3;
        int damage = 1;
        dummy.addEffect(new PeriodicDamageEffect(3, damage));

        for (int i = 1; i <= steps; i++)
        {
            dummy.endTurn();
            assertEquals(dummy.getHp()[1] - (i * damage), dummy.getHp()[0]);
        }
    }

    @Test
    public void healTest()
    {
        dummy.takeDamage(7, Resists.DamageType.PURE);
        assertEquals(1, dummy.getHp()[0]);
        new InstantHeal("Heal", 7).apply(dummy);
        assertEquals(8, dummy.getHp()[0]);
    }

    @Test
    public void attackTest()
    {
        dummy.addAttack(new MeleeAttack(5));
        Attack attack = dummy.getAttacks()[0];
        dummy.addAction(new AttackAction(dummy, target, 1, attack));
        // посмотрим, потратились ли очки действий
        assertEquals(0, dummy.getAP()[0]);

        // это действие не должно добавиться
        dummy.addAction(new AttackAction(dummy, target, 1, attack));

        dummy.performAction();
        assertEquals(3, target.getHp()[0]);
    }

    @Test
    public void vampirismTest()
    {
        dummy.takeDamage(7, Resists.DamageType.PURE);
        assertEquals(1, dummy.getHp()[0]);
        dummy.addAttack(new VampireBite(7));
        Attack attack = dummy.getAttacks()[0];

        dummy.addAction(new AttackAction(dummy, target, 1, attack));
        // посмотрим, потратились ли очки действий
        assertEquals(0, dummy.getAP()[0]);

        dummy.performAction();

        assertEquals(1, target.getHp()[0]);
        assertEquals(8, dummy.getHp()[0]);
    }

    @Test
    public void PeriodicStatsEffectTest()
    {
        HashMap<String, Integer> additionalStats = new HashMap<>();
        additionalStats.put("additionalHp", 10);
        additionalStats.put("agility", 9);

        dummy.addEffect(new PeriodicStatsEffect(3, "Тестовый бафф", new Stats(additionalStats)));

        for (int i = 0; i < 3; i++)
        {
            dummy.endTurn();
            assertEquals(10, dummy.getStat("agility"));
            assertEquals(18, dummy.getHp()[1]);
        }
        dummy.endTurn();
        assertEquals(1, dummy.getStat("agility"));
        assertEquals(8, dummy.getHp()[1]);
    }

    @Test
    public void locationTest()
    {
        assertEquals(0, dummy.getPosition().getX());
        assertEquals(0, dummy.getPosition().getY());
        assertEquals(1, target.getPosition().getX());
        assertEquals(1, target.getPosition().getY());

        assertEquals(dummy, location.getPosition(0, 0).getMember());
        assertEquals(target, location.getPosition(target.getPosition().getX(), target.getPosition().getY()).getMember());
    }

    @Test
    public void teleportationTest()
    {
        try
        {
            dummy.teleport(new Position(5, 5));
        } catch (Exception e)
        {
            fail(e.getMessage());
        }

        // проверяем, что после телепортации в предыдущей позиции ничего не осталось
        assertNull(location.getPosition(0, 0).getMember());

        // проверяем, что существо корректно разместилось в новой локации
        assertEquals(dummy, location.getPosition(5, 5).getMember());

        // проверяем позицию существа
        assertEquals(5, dummy.getPosition().getX());
        assertEquals(5, dummy.getPosition().getY());
    }

    @Test
    public void iconTest()
    {
        assertEquals('$', dummy.getIcon());
        assertEquals(dummy.getIcon(), dummy.getLocation().getPosition(dummy.getPosition().getX(), dummy.getPosition().getY()).getIcon());
    }

    @Test
    public void endTurnTest()
    {
        var stats = new Stats();
        stats.setStat("endurance", 10);
        dummy.addStats(stats);
        dummy.endTurn();
        assertEquals(5, dummy.getAP()[0]);

        dummy.addAttack(new MeleeAttack(5));
        Attack attack = dummy.getAttacks()[0];
        dummy.addAction(new AttackAction(dummy, target, 2, attack));
        dummy.addAction(new AttackAction(dummy, target, 2, attack));
        dummy.addAction(new AttackAction(dummy, target, 2, attack));
        assertEquals(1, dummy.getAP()[0]);

        dummy.endTurn();

        // все ли действия были выполнены
        assertEquals(0, dummy.getActions().length);
    }

    @Test
    public void moveTest()
    {
        dummy.addAction(new MoveAction(dummy, 1, new Position(2, 3)));
        dummy.endTurn();
        // проверяем, что существо корректно разместилось в новой локации
        assertEquals(dummy, location.getPosition(2, 3).getMember());

        // проверяем позицию существа
        assertEquals(2, dummy.getPosition().getX());
        assertEquals(3, dummy.getPosition().getY());
    }

    @Test
    public void pickupActionTest()
    {
        Item testItem = new Item("Kek", 1, 1);
        location.getPosition(dummy.getPosition()).addItem(testItem);
        assertEquals(testItem, location.getPosition(dummy.getPosition()).getItems()[0]);

        dummy.addAction(new PickupAction(dummy, testItem));
        assertEquals("Action.Pickup", dummy.getActions()[0].toString());

        dummy.doAllActions();

        assertEquals(0, location.getPosition(dummy.getPosition()).getItems().length);
        assertEquals(testItem, dummy.getInventory()[0]);
    }

    @Test
    public void equipActionTest()
    {
        BodyArmor armor = new BodyArmor("Доспех",1, 1, new Resists(), new Stats());
        location.getPosition(dummy.getPosition()).addItem(armor);
        assertEquals(1,location.getPosition(dummy.getPosition()).getItems().length);

        dummy.addAction(new PickupAction(dummy, armor));

        dummy.endTurn();

        dummy.addAction(new EquipAction(dummy, armor));

        dummy.endTurn();

        assertEquals(0,location.getPosition(dummy.getPosition()).getItems().length);
        assertEquals(0, dummy.getInventory().length);
        assertEquals(armor, dummy.getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BODY));
    }
}
