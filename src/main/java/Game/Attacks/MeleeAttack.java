package Game.Attacks;

import Game.Creature;
import Game.Effects.InstantDamage;
import Game.Interfaces.IEffect;
import Game.Resists.DamageType;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Простая атака ближнего боя
 */
public class MeleeAttack extends Attack
{
    public MeleeAttack(int damageAmount)
    {
        super("Атака ближнего боя", 1, new ArrayList<IEffect>(),
                new ArrayList<IEffect>(
                        Arrays.asList(new InstantDamage("Физический урон", DamageType.PHYSICAL, damageAmount))
                ), damageAmount
        );
    }
}
