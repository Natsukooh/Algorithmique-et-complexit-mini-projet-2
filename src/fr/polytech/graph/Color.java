package fr.polytech.graph;

import fr.polytech.exception.ColorFormatException;

public enum Color
{
    RED("Red"),
    BLUE("Blue");

    final String color;

    Color(String color)
    {
        this.color = color;
    }

    public static Color toColor(String color) throws ColorFormatException
    {
        if(color.equals("R") || color.equals("r"))
        {
            return RED;
        }
        else if(color.equals("B") || color.equals("b"))
        {
            return BLUE;
        }
        else
        {
            throw new ColorFormatException();
        }
    }

    @Override
    public String toString()
    {
        return color;
    }
}