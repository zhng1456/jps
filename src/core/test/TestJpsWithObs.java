package core.test;

import org.junit.Test;

import core.exception.MapInitialisationException;
import core.map.MapController;
import core.map.shortestpath.ShortestPathResult;
import core.util.Tuple2;
import core.util.Vector;

public class TestJpsWithObs {
    /**
     * ����JPS�㷨�������ϰ��������
     * @throws MapInitialisationException 
     */
    @Test
    public void testJPS() throws Exception{
        MapController controller = new MapController();
        controller.setJPSShortestPath();
        controller.setMapWithObs(new Vector(4,4),new Vector[]{new Vector(1,1),new Vector(2,2)});
        controller.setOrthogonalNeighborMovingRule();
        controller.setManhattanHeuristic();
        Tuple2<ShortestPathResult, Long> res = controller.runShortstPath(new Vector(0,0), new Vector(3,3));
        System.out.println("��ʱ" + res.getArg2());
        System.out.println("·��Ϊ:" + res.getArg1().getShortestPath());
    }
}
