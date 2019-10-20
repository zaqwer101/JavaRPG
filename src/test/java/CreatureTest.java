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
        dummy = new Creature("Dummy", '$', new Position(2, 2), location);
        target = new Creature("Target", 'T', new Position(3, 3), location);
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
        assertEquals(2, dummy.getPosition().getX());
        assertEquals(2, dummy.getPosition().getY());
        assertEquals(3, target.getPosition().getX());
        assertEquals(3, target.getPosition().getY());

        assertEquals(dummy, location.getPosition(2, 2).getMember());
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
        assertNull(location.getPosition(2, 2).getMember());

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
        // TODO проверить корректность работы с MP и AP при перемещении

        dummy.performAction(new MoveAction(dummy, new int[] {1, 0} ));
        // проверяем, что существо корректно разместилось в новой локации
        assertEquals(dummy, location.getPosition(3, 2).getMember());

        // проверяем позицию существа
        assertEquals(3, dummy.getPosition().getX());
        assertEquals(2, dummy.getPosition().getY());
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

    @Test
    public void periodicEffectsTest()
    {
        PeriodicStatsEffect effect1 = new PeriodicStatsEffect(2, "Test effect 1", new Stats());
        PeriodicStatsEffect effect2 = new PeriodicStatsEffect(2, "Test effect 2", new Stats());
        PeriodicStatsEffect effect3 = new PeriodicStatsEffect(2, "Test effect 3", new Stats());
        PeriodicStatsEffect effect4 = new PeriodicStatsEffect(2, "Test effect 4", new Stats());

        dummy.addEffect(effect1);
        dummy.addEffect(effect2);
        dummy.addEffect(effect3);
        dummy.addEffect(effect4);

        assertEquals(4, dummy.getEffects().length);
        assertEquals(2, dummy.getEffects()[0].getDuration());

        dummy.endTurn();

        assertEquals(4, dummy.getEffects().length);
        assertEquals(2, dummy.getEffects()[0].getDuration());

        dummy.endTurn();

        assertEquals(4, dummy.getEffects().length);
        assertEquals(1, dummy.getEffects()[0].getDuration());

        dummy.endTurn();

        assertEquals(0, dummy.getEffects().length);
    }
}
