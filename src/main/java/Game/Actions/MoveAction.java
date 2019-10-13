package Game.Actions;

import Game.Core.Creature;
import Game.Core.JavaRPG;
import Game.Core.Position;

public class MoveAction extends Action
{
    int[] direction;
    public MoveAction(Creature user, int[] direction)
    {
        super(user, ActionType.ACTION_MOVE, 0);
        this.direction = direction;
    }

    @Override
    public void use()
    {
        // TODO
        // Не тратит очки действия, тратит очки передвижения
    }
}
