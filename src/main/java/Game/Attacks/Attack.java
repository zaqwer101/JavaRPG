package Game.Attacks;

import Game.Core.Creature;
import Game.Effects.IEffect;

import java.util.ArrayList;

public abstract class Attack {
    ArrayList<IEffect> onSelf, onTarget;
    int attackDistance;
    String name;
    int damageAmount;
    public Attack(String name, int attackDistance, ArrayList<IEffect> onSelf, ArrayList<IEffect> onTarget, int damageAmount)
    {
        this.name = name;
        this.attackDistance = attackDistance;
        this.onTarget = onTarget;
        this.onSelf = onSelf;
        this.damageAmount = damageAmount;
    }

    public void attack(Creature attacker, Creature target)
    {
        if (attacker.getPosition().getDistance(target.getPosition()) <= this.attackDistance)
        {
            // onSelf.apply(attacker);
            for (var effect : onSelf)
            {
                effect.apply(attacker);
            }
            // onTarget.apply(target);
            for (var effect : onTarget)
            {
                effect.apply(target);
            }
        }
    }

    public String getName()
    {
        return name;
    }
}
