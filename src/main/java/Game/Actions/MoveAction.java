package Game.Actions;

import Game.Core.Creature;
import Game.Core.JavaRPG;
import Game.Core.Position;

public class MoveAction extends Action
{
    int[] direction;
    public MoveAction(Creature user, int cost, int[] direction)
    {
        super(user, ActionType.ACTION_MOVE, cost);
        this.direction = direction;
    }

    @Override
    public void use()
    {
        try
        {
            user.teleport(new Position(
                    user.getPosition().getX() + direction[0],
                    user.getPosition().getY() + direction[1]
            ));
        }
        catch (Exception e)
        {
            JavaRPG.log(e.getMessage());
        }
    }
}
