import java.util.*;

public class Minimax implements ISolver {
    @Override
    public String getSolverName() {
        return "Minimax";
    }

    @Override
    public double solve(IBoard board) {
        Node root = new Node(board, Node.NodeType.MAX);
        return MinimaxAlgorithm(root);
    }

    private double MinimaxAlgorithm(Node node) {
        if(node.isTerminalNode()){
            return node.getScore();
        }
        if(node.getNodeType().equals(Node.NodeType.MAX)){
            List<Node> childes = node.getNodeChildren();
            LinkedList<Double> maxes = new LinkedList<>();
            for (Node nd: childes
                 ) {
                maxes.add(MinimaxAlgorithm(nd));
            }
            return Collections.max(maxes);
        }
        else{
            List<Node> childes = node.getNodeChildren();
            LinkedList<Double> mines = new LinkedList<>();
            for (Node nd: childes
            ) {
                mines.add(MinimaxAlgorithm(nd));
            }
            return Collections.min(mines);
        }
    }

}
