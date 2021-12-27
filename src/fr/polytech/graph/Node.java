package fr.polytech.graph;

import fr.polytech.exception.EdgeAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Node extends Colored
{
    private final String name;
    private final List<Edge> incomingEdges;
    private List<Edge> outgoingEdges;

    public Node(Color color, String name)
    {
        super(color);
        this.name = name;
        this.outgoingEdges = new ArrayList<>();
        this.incomingEdges = new ArrayList<>();
    }

    public Node(Color color, String name, List<Edge> edges)
    {
        this(color, name);
        this.outgoingEdges = edges;
    }

    public void addOutgoingEdge(Edge edge) throws EdgeAlreadyExistsException
    {
        if(getNextNodeByName(edge.getEnd()
                .getName())
                .isPresent())
        {
            throw new EdgeAlreadyExistsException();
        }

        outgoingEdges.add(edge);
        edge.getEnd().incomingEdges.add(edge);
    }

    public Optional<Node> getNextNodeByName(String name)
    {
        return outgoingEdges
                .stream()
                .map(Edge::getEnd)
                .filter(end -> end.getName().equals(name))
                .findAny();
    }

    public String getName()
    {
        return name;
    }

    public List<Edge> getOutgoingEdges()
    {
        return outgoingEdges;
    }

    public List<Edge> getIncomingEdges()
    {
        return incomingEdges;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return name.equals(node.name) && outgoingEdges.equals(node.outgoingEdges);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, outgoingEdges);
    }

    @Override
    public String toString()
    {
        return "Node{" +
                "color=" + color +
                ", name='" + name + '\'' +
                ", edges=" + outgoingEdges +
                '}';
    }
}
