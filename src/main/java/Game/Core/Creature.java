package Game.Core;

import Game.Actions.Action;
import Game.Attacks.Attack;
import Game.Effects.Effect;
import Game.Effects.PeriodicEffect;
import Game.Core.Resists.DamageType;
import Game.Items.Equipment.Armor.BackpackArmor;
import Game.Items.Equipment.Equipment;
import Game.Items.Inventory;
import Game.Items.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Creature extends WorldObject
{
    private HashMap<Equipment.EquipmentSlot, Equipment> equipmentSlots;
    private ArrayList<PeriodicEffect> effects;
    private Inventory baseInventory; // карманы существа
    private Stats stats;
    private Resists resists;
    private Position position;
    private ArrayList<Attack> attacks;
    private Location location;
    private ArrayList<Action> actionQueue;
    private ArrayList<Effect> onAttackEffects, onTakeDamageEffects; // TODO
    /* Дефолтное существо имеет всех статов по 1
    = 8 hp
     */

    /**
     * Функция для конструктора, чтобы определённые статы не были == 0
     */
    public void checkBaseStats()
    {
        this.equipmentSlots = new HashMap<>();
        this.actionQueue = new ArrayList<>();
        this.baseInventory = new Inventory(20);
        for (var slot : Equipment.EquipmentSlot.values())
        {
            equipmentSlots.put(slot, null);
        }

        if (this.stats.getStat("level") <= 0)
            stats.setStat("level", 1);
        if (this.stats.getStat("strength") <= 0)
            stats.setStat("strength", 1);
        if (this.stats.getStat("agility") <= 0)
            stats.setStat("agility", 1);
        if (this.stats.getStat("intelligence") <= 0)
            stats.setStat("intelligence", 1);
        if (this.stats.getStat("endurance") <= 0)
            stats.setStat("endurance", 1);
    }

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
        onAttackEffects = new ArrayList<>();
        onTakeDamageEffects = new ArrayList<>();
        attacks = new ArrayList<>();
        resists = new Resists(new HashMap());
        stats = new Stats();
        effects = new ArrayList<>();
//        stats.setStat("level", 1);
//        stats.setStat("expToLevel", 100);
//        stats.setStat("strength", 1);
//        stats.setStat("agility", 1);
//        stats.setStat("intelligence", 1);
//        stats.setStat("endurance", 1);
        checkBaseStats();
        stats.setStat("baseHp", 0);
        stats.setStat("hp", stats.getStat("maxHp"));
        stats.setStat("actionPoints", stats.getStat("maxActionPoints"));

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
        this.checkBaseStats();
        effects = new ArrayList<>();
        this.stats.recountStats();
        onAttackEffects = new ArrayList<>();
        onTakeDamageEffects = new ArrayList<>();
        stats.setStat("hp", stats.getStat("maxHp"));
        stats.setStat("actionPoints", stats.getStat("maxActionPoints"));
    }

    public Position getPosition()
    {
        return position;
    }

    Stats getStats()
    {
        stats.recountStats();
        return stats;
    }

    public int getStat(String stat)
    {
        return stats.getStat(stat);
    }

    /**
     * Прибавить статы к статам существа
     *
     * @param stats
     */
    public void addStats(Stats stats)
    {
        this.stats = this.stats.add(stats);
        this.stats.recountStats();
    }

    /**
     * Добавить очки сопротивления урону
     */
    public void addResists(Resists resists)
    {
        this.resists = this.resists.add(resists);
    }

    public void subResists(Resists resists)
    {
        this.resists = this.resists.sub(resists);
    }


    /**
     * Отнять статы
     * @param stats
     */
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
     * @return массив [текущие HP, максимум HP]
     */
    public int[] getHp()
    {
        return new int[] {
                getStats().getStat("hp"),
                getStats().getStat("maxHp")
        };
    }

    /**
     * Получить очки действия
     * @return массив [текущие AP, максимум AP]
     */
    public int[] getAP()
    {
        return new int[] {
                getStats().getStat("actionPoints"),
                getStats().getStat("maxActionPoints")
        };
    }

    public void heal(int amount)
    {
        stats.setStat("hp", stats.getStat("hp") + amount);
        if (stats.getStat("hp") > stats.getStat("maxHp"))
        {
            stats.setStat("hp", stats.getStat("maxHp"));
        }
    }

    /**
     * Обработать периодические эффекты, висящие на существе
     */
    public void recountEffects()
    {
        for (int i = 0; i < effects.size(); i++)
        {
            effects.get(i).apply(this);
            // если длительность = -100, эффект без длительности
            if (effects.get(i).getDuration() <= 0 && effects.get(i).getDuration() != PeriodicEffect.FOREVER)
            {
                JavaRPG.log(this.getName() + ": эффект " + effects.get(i).getName() + " снят");
                effects.get(i).remove(this);
                effects.remove(effects.get(i));
            } else
                JavaRPG.log(this.getName() + ": " + effects.get(i).getName() + " осталось " + effects.get(i).getDuration() + " ходов");
        }
    }

    /**
     * Навесить на существо периодический эффект
     * @param effect
     */
    public void addEffect(PeriodicEffect effect)
    {
        effects.add(effect);
        JavaRPG.log(getName() + ": наложен эффект " + effect.getName());
    }

    /**
     * Нанести урон этому существу
     * @param amount
     * @param type
     */
    public void takeDamage(int amount, DamageType type)
    {
        int currentHp = getHp()[0];

        // fixed: сопротивления считались некорректно из-за того, что значения резистов - integer
        float coefficient = (float)resists.getResist(type) / 100;
        int blockedDamage = (int)(amount * coefficient);
        int damageTaken = amount - blockedDamage;
        getStats().setStat("hp", getHp()[0] - damageTaken);
    }

    /**
     * Добавить атаку в список доступных
     * @param attack
     */
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
        return attacks.toArray(new Attack[0]);
    }

    /**
     * Телепортация существа
     * @param position
     * @throws Exception
     */
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

    /**
     * Функция проверки, хватает ли очков для совершения действия
     * @return
     */
    public boolean spendActionPoints(int ap)
    {
        int currentAP = stats.getStat("actionPoints");

        if (currentAP >= ap)
        {
            int newAP = currentAP - ap;
            stats.setStat("actionPoints", newAP);
            return true;
        } else
            return false;
    }

    /**
     * Добавить действие в очередь
     * @param action
     */
    public void addAction(Action action)
    {
        if (spendActionPoints(action.getCost()))
            actionQueue.add(action);
        else
            JavaRPG.log(getName() + ": недостаточно очков дейстий для " + action.toString());
    }

    /**
     * Выполнить первое действие в списке
     */
    public void performAction()
    {
        actionQueue.get(0).use();
        actionQueue.remove(0);
    }

    /**
     * Выполнить все действия в очереди
     */
    public void doAllActions()
    {
        int size = actionQueue.size();
        for (int i = 0; i < size; i++)
        {
            JavaRPG.log(getName() + ": выполнил действие " + actionQueue.get(0).toString());
            performAction();
        }
    }

    /**
     * Завершить ход
     */
    public void endTurn()
    {
        doAllActions();
        stats.setStat("actionPoints", stats.getStat("maxActionPoints"));
        stats.recountStats();
        recountEffects();
    }

    public Action[] getActions()
    {
        return actionQueue.toArray(new Action[0]);
    }

    public void equip(Equipment equipment)
    {
        if (equipmentSlots.get(equipment.getSlot())!=null)
        {
            unEquip(equipmentSlots.get(equipment.getSlot()));
        }
        equipment.onEquip(this);
        equipmentSlots.replace(equipment.getSlot(), equipment);
        deleteFromInventory(equipment);
    }

    public void unEquip(Equipment equipment)
    {
        // TODO: выкладывать в инвентарь снаряжение при снятии
        equipment.onUnEquip(this);

        if (getMaxFreeSize() >= equipment.getSize() || getFreeWeight() >= equipment.getWeight())
        {
            addToInventory(equipment);
        }
        else
        {
            location.getPosition(position).addItem(equipment);
        }

        equipmentSlots.replace(equipment.getSlot(), null);

    }

    public int getCurrentWeight()
    {
        int weight = 0;
        for (var item : baseInventory.getInventory())
        {
            weight += item.getWeight();
        }

        if (getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK) != null)
            for (var item : ((BackpackArmor)getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).Inventory().getInventory())
            {
                weight += item.getWeight();
            }

        return weight;
    }

    public Equipment getEquipment(Equipment.EquipmentSlot slot)
    {
        return equipmentSlots.get(slot);
    }

    public boolean addToInventory(Item item)
    {
        if (getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK) != null)
        {
            if (
                    ((BackpackArmor)getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).
                            Inventory().getInventoryFreeSize() >= item.getSize()
            )
            {
                if(stats.getStat("maxWeight") >= (getCurrentWeight() + item.getWeight()))
                {
                    ((BackpackArmor)getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).addToInventory(item);
                    JavaRPG.log(getName() + ": предмет \"" + item.getName() + "\" добавлен в рюкзак " + getName());
                    return true;
                }
            }
        }
        else
        {
            if (baseInventory.getInventoryFreeSize() >= item.getSize())
            {
                if(stats.getStat("maxWeight") >= (getCurrentWeight() + item.getWeight()))
                {
                    baseInventory.addItem(item);
                    JavaRPG.log(getName() + ": предмет \"" + item.getName() + "\" добавлен в инвентарь ");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean pickUpItem(Item item)
    {
        if(!Arrays.asList(location.getPosition(position.getX(), position.getY()).getItems()).contains(item))
        {
            JavaRPG.log("Предмет \"" + item.getName() + "\" не найден в локации");
            return false;
        }
        if (addToInventory(item))
        {
            location.getPosition(position).removeItem(item);
            return true;
        }
        return false;
    }

    public boolean dropItem(Item item)
    {
        if(((BackpackArmor)getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).Inventory().getInventory().contains(item))
        {
            location.getPosition(position.getX(), position.getY()).addItem(item);
            JavaRPG.log("Предмет \"" + item.getName() + "\" выброшен в локацию");
            return true;
        }

        if(baseInventory.getInventory().contains(item))
        {
            location.getPosition(position.getX(), position.getY()).addItem(item);
            JavaRPG.log("Предмет \"" + item.getName() + "\" выброшен в локацию");
            return true;
        }
        return false;
    }

    public Item[] getInventory()
    {
        Item[] array;
        if (getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK) != null)
        {
            array = new Item[
                    baseInventory.getInventory().size() +
                            ((BackpackArmor) getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).Inventory().getInventory().size()
                    ];
            System.arraycopy(baseInventory.getInventory().toArray(), 0,
                    array, 0, baseInventory.getInventory().size());
            System.arraycopy( ((BackpackArmor)getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).Inventory().getInventory().toArray(), 0,
                    array, baseInventory.getInventory().size(), ((BackpackArmor)getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).Inventory().getInventory().size());
        }
        else
            array = baseInventory.getInventory().toArray(new Item[0]);

        return array;
    }

    public int getMaxFreeSize()
    {
        int max = 0;
        if (getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK) != null)
        {
            max = ((BackpackArmor)getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK)).Inventory().getInventoryFreeSize();
        }

        if (max < baseInventory.getInventoryFreeSize())
        {
            max = baseInventory.getInventoryFreeSize();
        }
        return max;
    }

    public int getFreeWeight()
    {
        return stats.getStat("maxWeight") - getCurrentWeight();
    }

    public void deleteFromInventory(Item item)
    {
        if (doesHaveBackpack())
        {
            for (int i = 0; i < getBackpack().Inventory().getInventory().size(); i++)
            {
                if (getBackpack().Inventory().getInventory().get(i) == item)
                {
                    getBackpack().Inventory().deleteItem(item);
                    return;
                }
            }
        }

        for (int i = 0; i < baseInventory.getInventory().size(); i++)
        {
            if (baseInventory.getInventory().get(i) == item)
            {
                baseInventory.deleteItem(item);
                return;
            }
        }
    }

    public boolean doesHaveBackpack()
    {
        return getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK) != null;
    }

    public BackpackArmor getBackpack()
    {
        return (BackpackArmor)getEquipment(Equipment.EquipmentSlot.EQUIPMENT_BACKPACK);
    }
}
