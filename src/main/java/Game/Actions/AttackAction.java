package Game.Actions;

import Game.Attacks.Attack;
import Game.Core.Creature;

public class AttackAction extends Action
{
    Attack attack;
    Creature target;

    public AttackAction(Creature user, Creature target, int cost, Attack attack)
    {
        super(user, ActionType.ACTION_ATTACK, cost);
        this.attack = attack;
        this.target = target;
    }

    @Override
    public boolean use()
    {
        if (attack.getAttackDistance() <= user.getPosition().getDistance(target.getPosition()))
        {
            attack.attack(user, target);
            return true;
        }
        else
            return false;
    }
}
