package Game.Attacks;

import Game.Effects.Effect;
import Game.Effects.InstantDamage;
import Game.Core.Resists.DamageType;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Простая атака ближнего боя
 */
public class MeleeAttack extends Attack
{
    public MeleeAttack(int damageAmount)
    {
        super("Атака ближнего боя", 1, new ArrayList<Effect>(),
                new ArrayList<Effect>(
                        Arrays.asList(new InstantDamage("Физический урон", DamageType.PHYSICAL, damageAmount))
                ), damageAmount
        );
    }

    public MeleeAttack(int damageAmount, String name)
    {
        super(name, 1, new ArrayList<Effect>(),
                new ArrayList<Effect>(
                        Arrays.asList(new InstantDamage("Физический урон", DamageType.PHYSICAL, damageAmount))
                ), damageAmount
        );
    }
}
