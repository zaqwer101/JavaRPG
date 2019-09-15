package Game.Effects;

import Game.Core.Creature;

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
        target.Heal(amount);
    }
}
