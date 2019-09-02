package Game.Attacks;

import Game.Creature;
import Game.Effects.Effect;
import Game.Effects.InstantHeal;

public class VampireBite extends MeleeAttack
{
    public VampireBite(int damageAmount)
    {
        super(damageAmount);
        this.onSelf.add(new InstantHeal("Вампиризм", damageAmount));
    }
}
