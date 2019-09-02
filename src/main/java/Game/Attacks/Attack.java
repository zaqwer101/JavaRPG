package Game.Attacks;

import Game.Creature;
import Game.Interfaces.IEffect;

public abstract class Attack {
    IEffect onSelf, onTarget;
    int attackDistance;
    String name;

    public void Attack(String name, int attackDistance, IEffect onSelf, IEffect onTarget)
    {
        this.name = name;
        this.attackDistance = attackDistance;
        this.onTarget = onTarget;
        this.onSelf = onSelf;
    }

    public void attack(Creature attacker, Creature target)
    {
        if (attacker.getPosition().getDistance(target.getPosition()) <= this.attackDistance)
        {
            onSelf.apply(attacker);
            onTarget.apply(target);
        }
    }
}
