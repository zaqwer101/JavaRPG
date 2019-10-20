package Game.Actions;

import Game.Core.Creature;
import Game.Items.Item;

public class PickupAction extends Action
{
    Item item;
    public PickupAction(Creature user, Item item)
    {
        super(user, ActionType.ACTION_PICKUP, 1);
        this.item = item;
    }

    @Override
    public boolean use()
    {
        user.pickUpItem(item);
        // TODO сделать так чтобы при невозможности выполнить действия возвращалось false
        return true;
    }
}
