package fr.polytech.graph;

/*
Abstract class for elements having a color attribute.
 */
public abstract class Colored
{
    protected Color color;

    public Colored(Color color)
    {
        this.color = color;
    }

    /*
    Makes the color attribute the opposite one.
     */
    public void switchColor()
    {
        this.color = this.color == Color.RED ? Color.BLUE : Color.RED;
    }

    /*
    Returns the color.
     */
    public Color getColor()
    {
        return color;
    }
}
