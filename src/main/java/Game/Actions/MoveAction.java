package Game.Actions;

import Game.Core.Creature;
import Game.Core.JavaRPG;
import Game.Core.Position;

public class MoveAction extends Action
{
    Position destination;
    public MoveAction(Creature user, int cost, Position destination)
    {
        super(user, ActionType.ACTION_MOVE, cost);
        this.destination = destination;
    }

    @Override
    public void use()
    {
        try
        {
            user.teleport(destination);
        }
        catch (Exception e)
        {
            JavaRPG.log(e.getMessage());
        }
    }
}
