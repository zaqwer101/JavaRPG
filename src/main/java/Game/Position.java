package Game;

/**
 * Позиция объекта в игровой локации
 */
public class Position {
    private int x, y;

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void add(Position position)
    {
        this.x += position.x;
        this.y += position.y;
    }

    public void sub(Position position)
    {
        this.x -= position.x;
        this.y -= position.y;
    }

    public int getDistance(Position position)
    {
        return (int) Math.sqrt(
                Math.pow(this.x - position.x, 2) + Math.pow(this.y - position.y, 2)
        );
    }
}