package Game.Core;

import Game.Attacks.Attack;
import Game.Effects.PeriodicEffect;
import Game.Core.Resists.DamageType;

import java.util.ArrayList;
import java.util.HashMap;

public class Creature extends WorldObject
{
    private ArrayList<PeriodicEffect> effects;
    private Stats stats;
    private Resists resists;
    private Position position;
    private ArrayList<Attack> attacks;
    private Location location;

    /* Дефолтное существо имеет всех статов по 1
    = 8 hp
     */
    public Creature(String name, char icon, Position position, Location location)
    {
        super(name, icon);
        this.location = location;
        try
        {
            teleport(position);
        } catch (Exception e)
        {
            this.location = null;
            this.position = null;
            JavaRPG.log(e.getMessage());
            return;
        }

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

    public Creature(String name, char icon, Position position, Stats stats, Location location)
    {
        super(name, icon);
        this.location = location;
        this.position = position;
        attacks = new ArrayList<>();
        resists = new Resists(new HashMap());
        this.stats = stats;
        effects = new ArrayList<>();
        this.stats.recountStats();
    }

    public Position getPosition()
    {
        return position;
    }

    // TODO: Доделать обработку баффов на статы
    public void recountEffects()
    {
        for (int i = 0; i < effects.size(); i++)
        {
            effects.get(i).apply(this);

            // если длительность = -100, эффект без длительности
            if(effects.get(i).getDuration() <= 0 && effects.get(i).getDuration() != PeriodicEffect.FOREVER)
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

    public void addStats(Stats stats)
    {
        this.stats = this.stats.add(stats);
        this.stats.recountStats();
    }

    public void subStats(Stats stats)
    {
        this.stats = this.stats.sub(stats);
        this.stats.recountStats();
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
                getStats().getStat("maxHp")
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

    public void passTurn()
    {
        this.stats.recountStats();
        recountEffects();
    }

    public void teleport(Position position) throws Exception
    {
        if (location.getPosition(position.getX(), position.getY()).isEmpty())
        {
            if(this.position != null)
                location.getPosition(this.position.getX(), this.position.getY()).setMember(null);

            this.position = position;
            location.getPosition(position.getX(), position.getY()).setMember(this);
        } else
            throw new Exception("Позиция занята");
    }

    /**
     * Переместить существо на определённое количество клеток
     * @param x
     * @param y
     */
    public void move(int x, int y)
    {
        // TODO: Реализовать после Position.findPath
    }

    public Location getLocation()
    {
        return location;
    }
}
