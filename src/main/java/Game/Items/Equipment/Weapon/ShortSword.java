package Game.Items.Equipment.Weapon;

import Game.Attacks.Attack;
import Game.Attacks.BaseAttack;
import Game.Attacks.MeleeAttack;
import Game.Core.Resists;
import Game.Core.Stats;

import java.util.ArrayList;
import java.util.List;

public class ShortSword extends Weapon
{
    public ShortSword(String name, int weight, int size, Resists resists, Stats stats, Stats requirements, int damage)
    {
        super(name, weight, size, resists, stats, requirements, new ArrayList<Attack>(List.of(new MeleeAttack(damage, "Атака мечом"))));
    }
}
