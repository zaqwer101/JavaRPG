package Game.Actions;

import Game.Attacks.Attack;
import Game.Core.Creature;
import Game.Core.JavaRPG;

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
        if (target != null)
        {
            if (target.isAlive())
            {
                if (attack.getAttackDistance() <= user.getPosition().getDistance(target.getPosition()))
                {
                    attack.attack(user, target);
                    JavaRPG.log(user.getName() + ": атаковал " + target.getName());
                    return true;
                }
                else
                    return false;
            }
            else
            {
                JavaRPG.log(user.getName() + ": цель " + target.getName() + " уже мертва");
                return false;
            }
        }
        else
        {
            JavaRPG.log(user.getName()+": некого атаковать");
            return false;
        }
    }
}
