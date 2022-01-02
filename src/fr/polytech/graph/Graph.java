package fr.polytech.graph;

import fr.polytech.exception.EdgeAlreadyExistsException;
import fr.polytech.exception.NodeNameAlreadyTakenException;
import fr.polytech.solver.RedBlueMaximizationSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
An object to represent a graph.
The only member this class has is a List of all the nodes it contains.
The edges can be accessed through the nodes : a Node object contains two lists of edges (one for the incoming edges and one for the outgoing ones).
 */
public class Graph
{
    private final List<Node> nodes;

    /*
    Builds an empty graph.
     */
    public Graph()
    {
        nodes = new ArrayList<>();
    }

    /*
    Builds a graph from a given list of nodes.
     */
    public Graph(List<Node> nodes)
    {
        this.nodes = nodes;
    }

    /*
    Returns the nodes list.
     */
    public List<Node> getNodes()
    {
        return nodes;
    }

    /*
    Adds a node to the graph.
    Since each node has a name, if the node in parameter has a name which is already taken; a NodeNameAlreadyTakenException will be thrown. Else, the node will be added.
     */
    public void addNode(Node node) throws NodeNameAlreadyTakenException
    {
        if (this.getNodeByName(node.getName())
                .isPresent())
        {
            throw new NodeNameAlreadyTakenException();
        }

        nodes.add(node);
    }

    /*
    Returns the node having the given name, wrapped in an Optional<Node> object.
    If there's no node having the given name, returns Optional::EMPTY.
    The returned result should always be checked with the Optional::isPresent().
     */
    public Optional<Node> getNodeByName(String name)
    {
        return nodes.stream()
                .filter(node -> node.getName().equals(name))
                .findAny();
    }

    /*
    Create a clone of this, removes the red node given in parameter applying the rules of color changing and returns the clone, wrapped in an Optional<Graph>.
    If the node in parameter is a blue node (it is forbidden to remove a blue node), returns Optional::EMPTY.
    The returned result should always be checked with the Optional::isPresent().
     */
    public Optional<Graph> popRedNodeAndPropagateRed(Node node)
    {
        if (node.getColor() != Color.RED)
        {
            System.err.println("Color error : unable to remove node called " + node.getName() + ", can only remove red nodes.");
            return Optional.empty();
        }

        Graph clone = null;

        try
        {
            clone = (Graph) this.clone();
        } catch (CloneNotSupportedException e)
        { /* this should not happen */ }

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

    /*
    Solves the graph applying the algorithm written in the Solver object passed in parameter.
    Returns the result of the solve function ; this is by convention the size of the best red sequence found by the algorithm.
     */
    public int solve(RedBlueMaximizationSolver solver)
    {
        return solver.solve(this);
    }

    /*
    toString function, not much to talk about, nothing exceptional happening here.
     */
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

    /*
    Clones the current graph (this) and returns the cloned graph.
    This is done by creating an empty graph, and creating a new node for each node the graph should contain, as well as the edges for each node.
    I'm not sure that this is java conventions friendly to clone an object this way, but it seems to work.
    No clue why (I'm not a java expert), but IntellIJ keeps giving me this warning : Method 'clone()' does not call 'super.clone()' .
    I tried to add an initial call to super.clone() because I like when I have 0 warning, but this resulted in an exception. Screw this, I deleted the super.clone() and now I have a painful warning I can't get rid of.
    The function signature has to include the 'throws CloneNotSupportedException', but since I don't make a call to super.clone(), I think this exception will never be thrown.
    */
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
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
