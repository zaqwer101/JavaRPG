package Game.Attacks;

import Game.Effects.InstantHeal;

public class VampireBite extends MeleeAttack
{
    public VampireBite(int damageAmount)
    {
        super(damageAmount);
        this.onSelf.add(new InstantHeal("Вампиризм", damageAmount));
    }
}
