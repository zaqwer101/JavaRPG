package Game.Actions;

import Game.Attacks.Attack;
import Game.Core.Creature;
import Game.Core.JavaRPG;
import Game.Core.WorldObject;

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
    public void use()
    {
        attack.attack(user, target);
    }
}
