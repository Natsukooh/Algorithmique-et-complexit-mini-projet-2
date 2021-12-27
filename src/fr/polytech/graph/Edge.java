package fr.polytech.graph;

import java.util.Objects;

public class Edge extends Colored
{
    private final Node begin;
    private final Node end;

    public Edge(Color color, Node begin, Node end)
    {
        super(color);
        this.begin = begin;
        this.end = end;
    }

    public Node getEnd()
    {
        return end;
    }

    public Node getBegin()
    {
        return begin;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return end.equals(edge.end);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(end);
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
