package core.test;

import org.junit.Test;

import core.exception.MapInitialisationException;
import core.map.MapController;
import core.map.shortestpath.ShortestPathResult;
import core.util.Tuple2;
import core.util.Vector;

public class TestJpsWithObs {
    /**
     * 测试JPS算法，在有障碍的情况下
     * @throws MapInitialisationException 
     */
    @Test
    public void testJPS() throws Exception{
        MapController controller = new MapController();
        controller.setJPSShortestPath();
        controller.setMapWithObs(new Vector(4,4),new Vector[]{new Vector(0,1),new Vector(2,1)});
        controller.setOrthogonalNeighborMovingRule();
        controller.setEuclideanHeuristic();
        Tuple2<ShortestPathResult, Long> res = controller.runShortstPath(new Vector(0,0), new Vector(3,3));
        System.out.println("耗时" + res.getArg2());
        System.out.println("路径为:" + res.getArg1().getShortestPath());
    }
}
