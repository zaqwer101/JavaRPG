package Game.Attacks;

import Game.Core.Creature;
import Game.Effects.Effect;

import java.util.ArrayList;

public class BaseAttack extends Attack
{
    public BaseAttack(Creature attacker)
    {
        super("Рукопашная атака", 1, null, null, attacker.getStat("strength"));
    }

}
