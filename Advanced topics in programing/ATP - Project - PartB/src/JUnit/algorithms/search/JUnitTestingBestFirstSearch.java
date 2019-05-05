/*
package algorithms.search;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JUnitTestingBestFirstSearch {

    @Test
    void solve() {

        for(int i =0; i<100; i++) {
            IMazeGenerator mg = new MyMazeGenerator();
            Maze maze = mg.generate(30, 30);
            SearchableMaze searchableMaze = new SearchableMaze(maze);

            ISearchingAlgorithm searcher = new BestFirstSearch();
            searcher.solve(searchableMaze);
            assertTrue(true);
        }


    }

}*/
