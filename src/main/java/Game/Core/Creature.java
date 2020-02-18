package Game.Core;

import Game.Actions.Action;
import Game.Attacks.Attack;
import Game.Attacks.MeleeAttack;
import Game.Core.Resists.DamageType;
import Game.Effects.Effect;
import Game.Effects.PeriodicEffect;
import Game.Items.Equipment.Armor.BackpackArmor;
import Game.Items.Equipment.Equipment;
import Game.Items.Equipment.Weapon.ITwoHanded;
import Game.Items.Equipment.Weapon.Weapon;
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
    private ArrayList<Attack> attacks;          // все атаки существа
    private ArrayList<Attack> baseArmedAttacks; // набор атак существа, не зависящий от оружия, но требующих его
    private ArrayList<Attack> baseAttacks;      // набор базовых атак
    private ArrayList<Attack> unarmedAttacks;   // набор базовых безоружных атак существа
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
        this.baseArmedAttacks = new ArrayList<>();
        this.unarmedAttacks = new ArrayList<>();
        this.baseAttacks = new ArrayList<>();

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
        checkBaseStats();
        stats.setStat("baseHp", 0);
        stats.setStat("hp", stats.getStat("maxHp"));
        stats.setStat("actionPoints", stats.getStat("maxActionPoints"));
        this.recountStats();
        recountAttacks();
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
        this.recountStats();
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
        this.recountStats();
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
        this.recountStats();
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
        this.recountStats();
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
        int size = effects.size();

        for (int i = size - 1; i >= 0; i--)
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

    private void recountStats()
    {
        stats.recountStats();
        recountEquipment();
    }

    private void recountEquipment()
    {
        for (var slot : Equipment.EquipmentSlot.values())
        {
            if (getEquipment(slot) != null &&
                    !getEquipment(slot).checkRequirements(this))
            {
                unEquip(getEquipment(slot));
            }
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

        if (!isAlive())
        {
            die();
        }
    }

    /**
     * Добавить атаку в список доступных
     * @param attack
     */
    public void addUnarmedAttack(Attack attack)
    {
        for (var a : unarmedAttacks)
        {
            if (attack.getName().equals(a.getName()))
            {
                unarmedAttacks.add(unarmedAttacks.indexOf(a), attack);
                return;
            }
        }
        unarmedAttacks.add(attack);
        recountAttacks();
    }

    public Attack[] getAttacks()
    {
        recountAttacks();
        return attacks.toArray(new Attack[0]);
    }

    // пересчитать список доступных атак
    public void recountAttacks()
    {
        this.attacks = new ArrayList<>();
        var weapons = getWeapons();

        if (weapons[0] == null) // значит без оружия
        {
            if (unarmedAttacks.size() == 0)
                attacks.add(new MeleeAttack(getStat("strength")));
            else
                this.attacks.addAll(this.unarmedAttacks);
        }
        else
        {
            for (int i = 0; i < weapons.length; i++)
            {
                if (weapons[i] != null)
                    this.attacks.addAll(weapons[i].getAttacks());
            }
            this.attacks.addAll(baseArmedAttacks);
        }
        this.attacks.addAll(baseAttacks);
    }
//
//    private Weapon[] getWeapons()
//    {
//        if (getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND) != null) // если в правой руке нет оружия - его нет совсем
//        {
//            if (((Weapon) getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND)).isTwoHanded())
//            {
//                return new Weapon[]{(Weapon) getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND)};
//            } else
//            {
//                if (getEquipment(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND) != null &&
//                        getEquipment(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND).getClass() == Weapon.class) // если оружие есть и в левой руке
//                {
//                    return new Weapon[]{(Weapon) getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND), (Weapon) getEquipment(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND)};
//                } else // если в левой руке оружия нет, есть только в правой
//                {
//                    return new Weapon[] { (Weapon) getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND)};
//                }
//            }
//        }
//        else
//            return new Weapon[] {};
//    }

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
        int currentAP = getAP()[0];

        if (currentAP >= ap)
        {
            this.addAP(- ap);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean performAction(Action action)
    {
        if(spendActionPoints(action.getCost()))
        {
            if (action.use())
            {
                return true;
            }
            else
            {
                JavaRPG.log(getName() + ": не удалось выполнить " + action.toString());
                addAP(action.getCost());
            }
        }
        else
        {
            JavaRPG.log(getName() + ": не хватило очков действий на " + action.toString());
            return false;
        }

        return false;
    }
    private void addAP(int cost)
    {
        var newAP = stats.getStat("actionPoints") + cost;
        if (getAP()[0] + newAP > getAP()[1])
        {
            stats.setStat("maxActionPoints", newAP);
            stats.setStat("actionPoints", newAP);
        }
        else
            stats.setStat("actionPoints", newAP);
    }


    /**
     * Завершить ход
     */
    public void endTurn()
    {
        JavaRPG.log(getName() + ": сдал ход");
        stats.setStat("actionPoints", stats.getStat("maxActionPoints"));
        this.recountStats();
        recountEffects();
    }

    public boolean equip(Equipment equipment)
    {
        Equipment.EquipmentSlot slot = equipment.getSlot();
        boolean isInInventory = false, isInLocation = false;
        // Если шмотка есть в инвентаре
        if (Arrays.asList(getInventory()).contains(equipment))
                isInInventory = true;
        // Если шмотка лежит в локации
        else
        {
            if (Arrays.asList(getLocation().getPosition(getPosition()).getItems()).contains(equipment))
                isInLocation = true;
            else
                return false; // шмотки нигде не оказалось
        }

        if (equipment.checkRequirements(this))
        {
            // если предмет надевается в руки
            if (equipment.getSlot() == Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND ||
                equipment.getSlot() == Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND)
            {
                // если уже надето двуручное, то ничего больше надеть нельзя
                if (getEquipment(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND) instanceof ITwoHanded ||
                        getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND) instanceof ITwoHanded)
                {
                    return false;
                }

                // если в обе руки (не только оружие) и существо не может носить двуручные в одной руке
                if (equipment instanceof ITwoHanded && !canEquipTwoHandedInOneHand())
                {
                    // тогда обе руки должны быть свободны
                    if (getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND) != null ||
                        getEquipment(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND)  != null)
                        return false;
                }
                // если одноручное, то надеваем в любую руку
                else
                {
                    // если нужный слот занят
                    if ((equipment.getSlot() == Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND && getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND) != null) ||
                            (equipment.getSlot() == Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND && getEquipment(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND) != null))
                    {
                        // если предмет для правой руки, но она занята, берём в левую
                        if (equipment.getSlot() == Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND && getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND) != null && getEquipment(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND) == null)
                        {
                            slot = Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND;
                        } else
                        {
                            // если предмет для левой руки, но она занята, берём в правую
                            if (equipment.getSlot() == Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND && getEquipment(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND) != null && getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND) == null)
                            {

                            } else
                                // если обе руки заняты, не берём
                                return false;
                        }
                    }
                }
            }

            if (getEquipment(slot) != null)
                return false;
            equipmentSlots.replace(slot, equipment);
            equipment.onEquip(this);
            recountAttacks();
        }
        else
            return false;

        if (isInInventory)
            deleteFromInventory(equipment);
        if (isInLocation)
            this.getLocation().getPosition(this.getPosition()).removeItem(equipment);

        return true;
    }

    public void unEquip(Equipment equipment)
    {
        equipment.onUnEquip(this);

        // убрать снаряжение в инвентарь или выбросить в локации
        if (getMaxFreeSize() >= equipment.getSize() || getFreeWeight() >= equipment.getWeight())
        {
            addToInventory(equipment);
        }
        else
        {
            location.getPosition(position).addItem(equipment);
        }

        if (getEquipment(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND) == equipment)
            equipmentSlots.replace(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND, null);
        else if (getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND) == equipment)
            equipmentSlots.replace(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND, null);
        else
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
        if (equipmentSlots == null)
            return null;
        else
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
    public PeriodicEffect[] getEffects()
    {
        return effects.toArray(new PeriodicEffect[0]);
    }

    public boolean spendMovementPoints(int points)
    {
        int currentMP = stats.getStat("movementPoints");

        if (currentMP >= points)
        {
            int newMP = currentMP - points;
            stats.setStat("movementPoints", newMP);
            return true;
        } else
            return false;
    }
    public int getMP()
    {
        return stats.getStat("movePoints");
    }

    public void addMP(int MP)
    {
        stats.setStat("movePoints", stats.getStat("movePoints") + MP);
    }

    public boolean isAlive()
    {
        return getHp()[0] > 0;
    }

    public void die()
    {
        stats.setStat("hp", 0);
        getLocation().getPosition(getPosition()).setMember(null);
        for (var item : getInventory())
            getLocation().getPosition(getPosition()).addItem(item);
        baseInventory = null;
        equipmentSlots = null;
        position = null;
        location = null;
    }

    /**
     * Получить надетое оружие в обеих руках
     * @return массив оружий, если 0 элемент == null, то оружие не надето
     */
    public Weapon[] getWeapons()
    {
        Weapon[] weapons = new Weapon[2];
        Weapon weapon1 = (Weapon)getEquipment(Equipment.EquipmentSlot.EQUIPMENT_RIGHTHAND);
        var weapon2 = (Weapon)getEquipment(Equipment.EquipmentSlot.EQUIPMENT_LEFTHAND);

        if (weapon1 == null)
        {
            if (weapon2 == null)
            {
                return new Weapon[] { null, null };
            } else {
                return new Weapon[] { weapon2, null };
            }
        } else {
            return new Weapon[] { weapon1, null };
        }
    }

    public boolean canEquipTwoHandedInOneHand()
    {
        return false;
    }
}
