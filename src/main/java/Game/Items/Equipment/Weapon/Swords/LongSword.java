package Game.Items.Equipment.Weapon.Swords;

import Game.Attacks.Attack;
import Game.Attacks.MeleeAttack;
import Game.Core.Resists;
import Game.Core.Stats;
import Game.Items.Equipment.Weapon.ITwoHanded;
import Game.Items.Equipment.Weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class LongSword extends Sword implements ITwoHanded {
    public LongSword(String name, int weight, int size, Resists resists, Stats stats, Stats requirements, int damage)
    {
        super(name, weight, size, resists, stats, requirements, damage);
        isTwoHanded = true;
    }
}
