package core.test;

import org.junit.Test;

import core.map.MapController;
import core.map.shortestpath.ShortestPathResult;
import core.util.Tuple2;
import core.util.Vector;

/**
 * ����һ����Ҫ���ڷ���ϵͳ�ϵ�BJPS�㷨
 * @author Durant
 *
 */
public class TestBoundedJPS {
    @Test
    public void testJPS() throws Exception{
        MapController controller = new MapController();
        controller.setJPSShortestPath();
        controller.setMapWithObs(new Vector(10,10),new Vector[]{new Vector(1,1),new Vector(2,2)});
        controller.setOrthogonalNeighborMovingRule();
        controller.setManhattanHeuristic();
        Tuple2<ShortestPathResult, Long> res = controller.runShortstPath(new Vector(9,0), new Vector(7,7));
        System.out.println("��ʱ" + res.getArg2());
        System.out.println("·��Ϊ:" + res.getArg1().getShortestPath());
    }
}
