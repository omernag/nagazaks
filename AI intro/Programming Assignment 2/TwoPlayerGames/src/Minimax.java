import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

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

    //getNodeChildren
    //getScore

    private double MinimaxAlgorithm(Node node) {
        if(node.isTerminalNode()){
            return node.getScore();
        }
        if(node.getNodeType().equals(Node.NodeType.MAX)){
            List<Node> childes = node.getNodeChildren();
            double max=Double.MIN_VALUE;
            for (Node nd: childes
                 ) {
                if(MinimaxAlgorithm(nd)>max){
                    max=nd.getScore();
                }
            }
            return max;
        }
        else{
            List<Node> childes = node.getNodeChildren();
            double min=Double.MAX_VALUE;
            for (Node nd: childes
            ) {
                if(MinimaxAlgorithm(nd)<min){
                    min=nd.getScore();
                }
            }
            return min;
        }
    }

}
