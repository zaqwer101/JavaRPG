package Game.Items.Equipment.Weapon.Swords;

import Game.Attacks.Attack;
import Game.Attacks.MeleeAttack;
import Game.Core.Resists;
import Game.Core.Stats;
import Game.Items.Equipment.Weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

abstract class Sword extends Weapon {
    Sword(String name, int weight, int size, Resists resists, Stats stats, Stats requirements, int damage)
    {
        super(name, weight, size, resists, stats, requirements, new ArrayList<Attack>(List.of(new MeleeAttack(damage, "Атака мечом"))));
    }
}
