package fr.polytech.graph;

import fr.polytech.exception.EdgeAlreadyExistsException;
import fr.polytech.exception.NodeNameAlreadyTakenException;
import fr.polytech.solver.RedBlueMaximizationSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Graph
{
    private final List<Node> nodes;

    public Graph()
    {
        nodes = new ArrayList<>();
    }

    public Graph(List<Node> nodes)
    {
        this.nodes = nodes;
    }

    public List<Node> getNodes()
    {
        return nodes;
    }

    public void addNode(Node node) throws NodeNameAlreadyTakenException
    {
        if (this.getNodeByName(node.getName())
                .isPresent())
        {
            throw new NodeNameAlreadyTakenException();
        }

        nodes.add(node);
    }

    public Optional<Node> getNodeByName(String name)
    {
        return nodes.stream()
                .filter(node -> node.getName().equals(name))
                .findAny();
    }

    public Optional<Graph> popRedNodeAndPropagateRed(Node node)
    {
        if (node.getColor() != Color.RED)
        {
            System.err.println("Color error : unable to remove node called " + node.getName() + ", can only remove red nodes.");
            return Optional.empty();
        }

        Graph clone;

        try
        {
            clone = (Graph) this.clone();
        } catch (CloneNotSupportedException ignored)
        { /* this should not happen */
            return Optional.empty();
        }

        //noinspection OptionalGetWithoutIsPresent
        Node nodeInClone = clone.getNodeByName(node.getName()).get();
        nodeInClone.getOutgoingEdges()
                .forEach(edge ->
                {
                    edge.getEnd().setColor(edge.getColor());
                    edge.getEnd()
                            .getIncomingEdges()
                            .remove(edge);
                });
        nodeInClone.getIncomingEdges()
                .forEach(edge ->
                        edge.getBegin()
                                .getOutgoingEdges()
                                .remove(edge));
        clone.nodes.remove(nodeInClone);

        return Optional.of(clone);
    }

    public int solve(RedBlueMaximizationSolver solver)
    {
        return solver.solve(this);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for (Node node : nodes)
        {
            sb.append("Name: ").append(node.getName()).append(", Color: ").append(node.getColor()).append("\n");

            if (node.getIncomingEdges().size() == 0)
            {
                sb.append("No incoming edge.\n");
            } else
            {
                for (Edge edge : node.getIncomingEdges())
                {
                    sb.append(edge.getBegin().getName()).append(" --").append(edge.getColor() == Color.RED ? "R" : "B").append("--> ").append(node.getName()).append("\n");
                }
            }

            if (node.getOutgoingEdges().size() == 0)
            {
                sb.append("No outgoing edge.\n");
            } else
            {
                for (Edge edge : node.getOutgoingEdges())
                {
                    sb.append(node.getName()).append(" --").append(edge.getColor() == Color.RED ? "R" : "B").append("--> ").append(edge.getEnd().getName()).append("\n");
                }
            }

            sb.append("==============================\n");
        }

        return sb.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        Object object = super.clone(); // if this line is deleted, IntellIJ puts a warning here... I don't get it but I don't like having warnings LMAO
        Graph clone = new Graph();

        this.nodes.forEach(node ->
        {
            try
            {
                clone.addNode(new Node(node.getColor(), node.getName()));
            } catch (NodeNameAlreadyTakenException ignored)
            { /* this should not happen */ }
        });

        this.nodes.forEach(node ->
                node.getOutgoingEdges().forEach(edge ->
                {
                    try
                    {
                        Optional<Node> nodeInCloneOptional = clone.getNodeByName(node.getName());

                        if (nodeInCloneOptional.isPresent())
                        {
                            Node nodeInClone = nodeInCloneOptional.get();
                            Optional<Node> endInCloneOptional = clone.getNodeByName(edge.getEnd().getName());

                            if (endInCloneOptional.isPresent())
                            {
                                Node endInClone = endInCloneOptional.get();
                                Edge newEdgeInClone = new Edge(edge.getColor(), nodeInClone, endInClone);
                                nodeInClone.addOutgoingEdge(newEdgeInClone);
                            }
                        }
                    } catch (EdgeAlreadyExistsException ignored)
                    { /* this should not happen */ }
                }));

        return clone;
    }
}
