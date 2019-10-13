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

    // Не тратит очки действия, тратит очки передвижения
    @Override
    public void use()
    {
        if (
                user.getLocation().getPosition(
                user.getPosition().getX() + direction[0],
                user.getPosition().getY() + direction[1]
                ).isPassable() // если точка проходима
        )
        {
            if (user.getStat("movePoints") >= user.getLocation().getPosition(
                    user.getPosition().getX() + direction[0],
                    user.getPosition().getY() + direction[1]
            ).getMovementCost()) // если существу хватает очков передвижения
            {
                user.spendActionPoints(user.getLocation().getPosition(
                        user.getPosition().getX() + direction[0],
                        user.getPosition().getY() + direction[1]
                ).getMovementCost());
                try
                {
                    user.teleport(new Position(user.getPosition().getX() + direction[0],
                            user.getPosition().getY() + direction[1]));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                // TODO
                // Я хз как корректно конвертировать очки
            }
        }
    }
}
