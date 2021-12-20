package fr.polytech.graph;

public abstract class Colored
{
    protected Color color;

    public Colored(Color color)
    {
        this.color = color;
    }

    public void switchColor()
    {
        this.color = this.color == Color.RED ? Color.BLUE : Color.RED;
    }

    public Color getColor()
    {
        return color;
    }
}
