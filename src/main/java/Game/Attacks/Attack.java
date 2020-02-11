package Game.Attacks;

import Game.Core.Creature;
import Game.Effects.Effect;

import java.util.ArrayList;

public abstract class Attack
{
    ArrayList<Effect> onSelf, onTarget;
    int attackDistance;
    String name;
    int damageAmount;

    public Attack(String name, int attackDistance, ArrayList<Effect> onSelf, ArrayList<Effect> onTarget, int damageAmount)
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
    public int getAttackDistance()
    {
        return attackDistance;
    }
    public int getDamage() { return damageAmount; };
}
