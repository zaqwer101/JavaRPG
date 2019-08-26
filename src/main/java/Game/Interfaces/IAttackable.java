package Game.Interfaces;

import Game.Resists.DamageType;

/**
 * Может ли объект выступать в качестве цели для атаки
 */
public interface IAttackable {
    int takeDamage(int damageAmount, DamageType damageType);
}
