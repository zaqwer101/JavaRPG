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
    public boolean use()
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
//                user.spendActionPoints(user.getLocation().getPosition( // Чтобы не тратить очки действия, если есть очки передвижения
//                        user.getPosition().getX() + direction[0],
//                        user.getPosition().getY() + direction[1]
//                ).getMovementCost());
                try
                {
                    user.teleport(new Position(user.getPosition().getX() + direction[0],
                            user.getPosition().getY() + direction[1]));
                    JavaRPG.log(user.getName() + ": выполнен переход в " + user.getPosition().getX() + "/" + user.getPosition().getY());
                    // отнимаем MP
                    user.addMP(-1 * user.getLocation().getPosition(
                            user.getPosition().getX() + direction[0],
                            user.getPosition().getY() + direction[1]).getMovementCost());
                    return true;
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else // если существу НЕ хватает очков передвижения
            {
                // считаем, сколько MP не хватает для перехода, считаем, сколько AP потребуется для этого конвертировать
                // конвертируем, переходим

                // немного говнокода для лучшего понимания :/
                int currentMP = user.getMP();
                int needMP = user.getLocation().getPosition(
                        user.getPosition().getX() + direction[0],
                        user.getPosition().getY() + direction[1]).getMovementCost() - currentMP;
                int needAP = (int)Math.ceil((double)needMP / (double)user.getStat("movePointsPerAP"));

                if (user.spendActionPoints(needAP)) // если существу хватает очков действия
                {
                    user.addMP(needAP * user.getStat("movePointsPerAP")); // добавляем недостающее количество MP
                    try
                    {
                        user.teleport(new Position(user.getPosition().getX() + direction[0],
                                user.getPosition().getY() + direction[1])); // перемещаем существо
                        JavaRPG.log(user.getName() + ": выполнен переход в " + user.getPosition().getX() + "/" + user.getPosition().getY());
                        // отнимаем MP
                        user.addMP(-1 * user.getLocation().getPosition(
                                user.getPosition().getX() + direction[0],
                                user.getPosition().getY() + direction[1]).getMovementCost());
                        return true;
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        else
        {
            JavaRPG.log(user.getName() + " точка недоступна для перехода");
            return false;
        }
        return false;

        // TODO сделать так чтобы при невозможности выполнить действия возвращалось false
    }
}
