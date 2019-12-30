import java.util.List;

public class AlphaBetaPruning implements ISolver {
    @Override
    public String getSolverName() {
        return "Alpha-Beta Pruning";
    }

    @Override
    public double solve(IBoard board) {
        Node root = new Node(board, Node.NodeType.MAX);
        return AlphaBetaPruningAlgorithm(root, -Double.MAX_VALUE, Double.MAX_VALUE);
    }

    private double AlphaBetaPruningAlgorithm(Node node, double p_alpha, double p_beta) {
        if(node.isTerminalNode()){
            return node.getScore();
        }
        if(node.getNodeType().equals(Node.NodeType.MAX)){
            List<Node> childes = node.getNodeChildren();
            double value = Double.NEGATIVE_INFINITY;
            for (Node nd: childes
            ) {
                value=Math.max(value,AlphaBetaPruningAlgorithm(nd,p_alpha,p_beta));
                p_alpha=Math.max(value,p_alpha);
                if(p_alpha >= p_beta){
                    return value;
                }
            }
            return value;
        }
        else{
            List<Node> childes = node.getNodeChildren();
            double value = Double.POSITIVE_INFINITY;
            for (Node nd: childes
            ) {
                value=Math.min(value,AlphaBetaPruningAlgorithm(nd,p_alpha,p_beta));
                p_beta=Math.min(value,p_beta);
                if(p_alpha >= p_beta){
                    return value;
                }
            }
            return value;
        }
    }
}
