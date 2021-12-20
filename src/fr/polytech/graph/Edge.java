package fr.polytech.graph;

public class Edge extends Colored
{
    private final Node end;

    public Edge(Color color, Node end)
    {
        super(color);
        this.end = end;
    }

    public Node getEnd()
    {
        return end;
    }

    @Override
    public String toString()
    {
        return "Edge{" +
                "color=" + color +
                ", end=" + end +
                '}';
    }
}
