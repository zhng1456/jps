package core.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.exception.MapInitialisationException;
import core.exception.NoPathFoundException;
import core.map.MapController;
import core.map.shortestpath.ShortestPathResult;
import core.util.Tuple2;
import core.util.Vector;

/**
 * ����һ�¸����㷨�ĵ��÷���,ʹ�ÿյ�ͼ,���ϰ�;500x500��ͼ,�����Ͻ��ܵ����½�
 * 
 * @author Durant
 *
 */
public class TestAlgorithm {
    private MapController controller = new MapController();

    /**
     * ��ʼ���������㷨�Լ������
     * 
     * @throws MapInitialisationException
     */
    @Before
    public void init() throws MapInitialisationException {
        controller.setEuclideanHeuristic();// ʹ��ŷʽ����
        controller.setEmptyMap(new Vector(500, 500));// ��500x500�ĵ�ͼ��
        controller.setOrthogonalNeighborMovingRule();// ֻ����ֱ�ƶ�
    }

    /**
     * A*�㷨,��Լ760ms
     */
    @Test
    public void setAstar() {
        controller.setAStarShortestPath();// ʹ��A*�㷨
    }

    /**
     * JPS�㷨,��Լ220ms
     */
    @Test
    public void setJPS() {
        controller.setJPSShortestPath();
    }

    /**
     * JPS plus�㷨,����Ԥ����,����85ms;Ԥ����ʱ������Ҫ2s
     */
    @Test
    public void setJPSPlus() {
        controller.setJPSPlusShortestPath();
        // jsp plus��ҪԤ����
        long preTime = controller.preprocessShortestPath();
        System.out.println("Ԥ����ʱ��:" + preTime);
    }

    /**
     * ִ��Ѱ·������
     * 
     * @throws Exception
     */
    @After
    public void printRes() {
        Tuple2<ShortestPathResult, Long> res = null;
        try {
            res = controller.runShortstPath(new Vector(0, 0), new Vector(499, 499));
        } catch (NoPathFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(res.getArg1().getShortestPath());
        System.out.println(res.getArg2());
    }
}
