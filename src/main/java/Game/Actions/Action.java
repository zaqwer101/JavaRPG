package Game.Actions;

import Game.Attacks.Attack;
import Game.Core.*;

public abstract class Action
{
    public static enum ActionType { ACTION_MOVE, ACTION_ATTACK };
    int cost; // стоимость действия
    WorldObject user; // кто вызвал действие
    ActionType type;

    public Action(WorldObject user, ActionType type, int cost)
    {
        this.type = type;
        this.user = user;
        this.cost = cost;
    }

    public abstract void use();
}
