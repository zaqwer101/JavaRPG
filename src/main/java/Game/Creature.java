package Game;

import Game.Attacks.Attack;
import Game.Effects.PeriodicEffect;
import Game.Resists.DamageType;
import java.util.ArrayList;
import java.util.HashMap;

public class Creature extends WorldObject
{
    private ArrayList<PeriodicEffect> effects;
    private Stats stats;
    private Resists resists;
    private Position position;
    private ArrayList<Attack> attacks;

    /* Дефолтное существо имеет всех статов по 1
    = 8 hp
     */
    public Creature(String name, char icon, Position position) {
        super(name, icon);
        this.position = position;
        attacks = new ArrayList<>();
        resists = new Resists(new HashMap());
        stats = new Stats();
        effects = new ArrayList<>();
        stats.setStat("level", 1);
        stats.setStat("expToLevel", 100);
        stats.setStat("strength", 1);
        stats.setStat("agility", 1);
        stats.setStat("intelligence", 1);
        stats.setStat("baseHp", 0);
        stats.setStat("hp", stats.getStat("maxHp"));
        stats.recountStats();
    }

    public Position getPosition()
    {
        return position;
    }

    public void recountEffects()
    {
        for (int i = 0; i < effects.size(); i++)
        {
            effects.get(i).apply(this);

            // если длительность = -100, эффект без длительности
            if(effects.get(i).getDuration() <= 0 && effects.get(i).getDuration() != -100)
            {
                effects.get(i).remove(this);
                effects.remove(effects.get(i));
            }
        }
    }

    public Stats getStats()
    {
        stats.recountStats();
        return stats;
    }

    public Resists getResists()
    {
        return resists;
    }

    /**
     * Получить очки здоровья существа
     * @return массив, где 0 - текущее количество очков, 1 - максимальное
     */
    public int[] getHp()
    {
        return new int[] {
                getStats().getStat("hp"),
                getStats().getStat("maxhp")
        };
    }

    public void Heal(int amount)
    {
        stats.setStat("hp", stats.getStat("hp") + amount);
        if (stats.getStat("hp") > stats.getStat("maxHp"))
        {
            stats.setStat("hp", stats.getStat("maxHp"));
        }
    }

    public void addEffect(PeriodicEffect effect)
    {
        effects.add(effect);
    }

    public void takeDamage(int amount, DamageType type)
    {
        getStats().setStat("hp",
                getStats().getStat("hp") - (amount - amount * (resists.getResist(type) / 100))
                );
    }

    public void addAttack(Attack attack)
    {
        for (var a : attacks)
        {
            if (attack.getName().equals(a.getName()))
            {
                attacks.add(attacks.indexOf(a), attack);
                return;
            }
        }
        attacks.add(attack);
    }

    public Attack[] getAttacks()
    {
        return (Attack[]) attacks.toArray();
    }

    public void useAttack(Attack attack, Creature target)
    {
        attacks.get(attacks.indexOf(attack)).attack(this, target);
    }

    public void useAttack(int index, Creature target)
    {
        attacks.get(index).attack(this, target);
    }

    //TODO получение уровня, получение экспы
}
