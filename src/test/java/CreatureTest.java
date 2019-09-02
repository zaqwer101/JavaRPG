import Game.Creature;
import Game.Effects.Bleeding;
import Game.Resists;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreatureTest {
    Creature dummy;

    @Before
    public void init()
    {
        dummy = new Creature("Dummy", '$');
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
        dummy.addEffect(new Bleeding(1, 2));
        dummy.recountEffects();
        assertEquals(6, dummy.getHp()[0]);
    }
}
