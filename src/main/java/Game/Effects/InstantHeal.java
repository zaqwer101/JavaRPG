package Game.Effects;

import Game.Core.Creature;
import Game.Core.JavaRPG;

/**
 * Мгновенный эффект, лечащий цель
 */
public class InstantHeal extends InstantEffect
{
    public InstantHeal(String name, int amount)
    {
        super(name, amount);
    }

    @Override
    public void apply(Creature target)
    {
        target.heal(amount);
        JavaRPG.log(target.getName() + ": вылечился от эффекта \"" + name + "\" на " + amount);
    }
}
