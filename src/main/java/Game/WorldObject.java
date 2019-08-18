package Game;

public abstract class WorldObject
{
    private static int _id = 0;
    private int id;
    private String name;
    private char icon;

    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public char getIcon()
    {
        return icon;
    }

    public WorldObject(String name, char icon)
    {
        this.name = name;
        this.icon = icon;
        this.id = _id ++;
    }
}
