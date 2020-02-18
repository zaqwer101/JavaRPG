package Game.Core;

import Game.Items.Item;

import java.util.ArrayList;

public class GlobalMapPosition {
    private char icon;                  // отображаемая на глобальной карте иконка
    private int x, y;                   // координаты точки
    private ArrayList<Party> members;   // отряды, находящиеся в точке карты

    /**
     * предметы, находящиеся в точке карты.
     * Пока не уверен как с ними будут взаимодействовать партии
     */
    private ArrayList<Item> items;

    public GlobalMapPosition(int x, int y, char icon)
    {
        members = new ArrayList<>();
        items = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.icon = icon;
    }

    public char getIcon()
    {
        return icon;
    }

    public int[] getCoordinates()
    {
        return new int[]{this.x, this.y};
    }

    public Party[] getMembers()
    {
        return (Party[]) members.toArray();
    }
}
