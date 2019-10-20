package Game.Actions;

import Game.Core.Creature;
import Game.Items.Equipment.Equipment;

public class EquipAction extends Action
{
    Equipment equipment;
    public EquipAction(Creature user, Equipment equipment)
    {
        super(user, ActionType.ACTION_EQUIP, 1);
        this.equipment = equipment;
    }

    @Override
    public boolean use()
    {
        this.user.equip(equipment);
        // TODO сделать так чтобы при невозможности выполнить действия возвращалось false
        return true;
    }
}
