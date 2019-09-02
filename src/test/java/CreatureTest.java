import Game.Attacks.MeleeAttack;
import Game.Attacks.VampireBite;
import Game.Creature;
import Game.Effects.Bleeding;
import Game.Effects.InstantHeal;
import Game.Effects.PeriodicStatsEffect;
import Game.Position;
import Game.Resists;
import Game.Stats;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreatureTest {
    Creature dummy;
    Creature target;
    @Before
    public void init()
    {
        dummy = new Creature("Dummy", '$', new Position(0, 0));
        target = new Creature("Target", 'D', new Position(1, 1));
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
            dummy.turn();
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
            dummy.turn();
            assertEquals(10, dummy.getStats().getStat("agility"));
            assertEquals(18, dummy.getHp()[1]);
        }
        dummy.turn();
        assertEquals(1, dummy.getStats().getStat("agility"));
        assertEquals(8, dummy.getHp()[1]);
    }
}
