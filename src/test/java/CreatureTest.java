import Game.Creature;
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

    }
}
