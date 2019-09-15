package Game.Actions;

import Game.Attacks.Attack;
import Game.Core.Creature;
import Game.Core.JavaRPG;
import Game.Core.WorldObject;

public abstract class AttackAction extends Action
{
    Attack attack;
    Creature target;
    public AttackAction(Creature user, Creature target, int cost, Attack attack)
    {
        super(user, ActionType.ACTION_ATTACK, cost);
        this.user = (Creature)user;
        this.attack = attack;
        this.target = target;
    }

    @Override
    public void use()
    {
        if (((Creature)user).spendActionPoints(cost))
            attack.attack((Creature)user, target);
        else
            JavaRPG.log(user.getName()+": недостаточно очков действий для атаки");
    }
}
