import Game.Attacks.MeleeAttack;
import Game.Attacks.VampireBite;
import Game.Core.*;
import Game.Effects.Bleeding;
import Game.Effects.InstantHeal;
import Game.Effects.PeriodicStatsEffect;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CreatureTest {
    Creature dummy;
    Creature target;
    Location location;
    @Before
    public void init()
    {
        location = new Location(10, 10);
        dummy = new Creature("Dummy", '$', new Position(0, 0), location);
        target = new Creature("Target", 'D', new Position(1, 1), location);
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
        dummy.addEffect(new Bleeding(3, damage));

        for (int i = 1; i < steps; i++ )
        {
            dummy.passTurn();
            assertEquals(dummy.getHp()[1] - (i * damage), dummy.getHp()[0]);
        }
    }

    @Test
    public void attackTest()
    {
        dummy.addAttack(new MeleeAttack(5));
        dummy.useAttack(0, target);
        assertEquals(3, target.getHp()[0]);
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
    public void vampirismTest()
    {
        dummy.takeDamage(7, Resists.DamageType.PURE);
        assertEquals(1, dummy.getHp()[0]);
        dummy.addAttack(new VampireBite(7));
        dummy.useAttack(0, target);
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

        for (int i = 0; i < 2; i++)
        {
            dummy.passTurn();
            assertEquals(10, dummy.getStats().getStat("agility"));
            assertEquals(18, dummy.getHp()[1]);
        }
        dummy.passTurn();
        assertEquals(1, dummy.getStats().getStat("agility"));
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

        assertEquals(null, location.getPosition(0, 0).getMember());
        assertEquals(dummy, location.getPosition(5, 5).getMember());
        assertEquals(5, dummy.getPosition().getX());
        assertEquals(5, dummy.getPosition().getY());
    }
}
