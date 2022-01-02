package fr.polytech.graph;

import fr.polytech.exception.ColorFormatException;

/*
Color enum.
Holds its String representation for printing.
 */
public enum Color
{
    RED("Red"),
    BLUE("Blue");

    final String color;

    Color(String color)
    {
        this.color = color;
    }

    /*
    Takes a string, and checks if it is equals to B for blue or R for red.
    If it matches one of the two, returns the associated color. Else, throws a ColorFormatException.
     */
    public static Color toColor(String color) throws ColorFormatException
    {
        if(color.equalsIgnoreCase("r"))
        {
            return RED;
        }
        else if(color.equalsIgnoreCase("b"))
        {
            return BLUE;
        }
        else
        {
            throw new ColorFormatException();
        }
    }

    /*
    Conventional equals function.
     */
    @Override
    public String toString()
    {
        return color;
    }
}
