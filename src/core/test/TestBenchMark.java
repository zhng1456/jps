package core.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.exception.MapInitialisationException;
import core.exception.NoPathFoundException;
import core.map.MapController;
import core.map.shortestpath.ShortestPathResult;
import core.util.TaskUtil;
import core.util.Tuple2;
import core.util.Vector;

public class TestBenchMark {
    private MapController controller = new MapController();

    /**
     * ��ʼ���������㷨�Լ������
     * 
     * @throws MapInitialisationException
     */
    @Before
    public void init() throws MapInitialisationException {
        controller.setEuclideanHeuristic();// ʹ��ŷʽ����
        controller.setRandomMap(new Vector(500,500), 0.9);// ��500x500�ĵ�ͼ��,�ϰ������0.1
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
        int taskNum = 100;
        TaskUtil util = new TaskUtil();
        List<List<Vector>> taskList = util.getRandomTask(controller.getMap(),taskNum);
        long averageTime = 0;
        long maxTime = -1;
        for(List<Vector> task : taskList){
            Tuple2<ShortestPathResult, Long> res = null;
            try {
                res = controller.runShortstPath(task.get(0), task.get(1));
                System.out.println("�������Ϊ:" + task.get(0) +"  �����յ�Ϊ:"+task.get(1));
                long time = res.getArg2();
                averageTime += time;
                maxTime = Math.max(maxTime, time);
                System.out.println("·������: " + res.getArg1().getShortestPath().size());
                System.out.println("Ѱ·ʱ��: " + res.getArg2());
            } catch (NoPathFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ƽ��ʱ��: " + (double)(averageTime/taskNum));
        System.out.println("�ʱ��: " + maxTime);
    }
}
