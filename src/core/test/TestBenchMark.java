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
     * 初始化，设置算法以及规则等
     * 
     * @throws MapInitialisationException
     */
    @Before
    public void init() throws MapInitialisationException {
        controller.setEuclideanHeuristic();// 使用欧式距离
        controller.setRandomMap(new Vector(500,500), 0.9);// 在500x500的地图上,障碍物比例0.1
        controller.setOrthogonalNeighborMovingRule();// 只允许垂直移动
    }

    /**
     * A*算法,大约760ms
     */
    @Test
    public void setAstar() {
        controller.setAStarShortestPath();// 使用A*算法
    }

    /**
     * JPS算法,大约220ms
     */
    @Test
    public void setJPS() {
        controller.setJPSShortestPath();
    }

    /**
     * JPS plus算法,不算预处理,跑完85ms;预处理时间大概需要2s
     */
    @Test
    public void setJPSPlus() {
        controller.setJPSPlusShortestPath();
        // jsp plus需要预处理
        long preTime = controller.preprocessShortestPath();
        System.out.println("预处理时间:" + preTime);
    }

    /**
     * 执行寻路输出结果
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
                System.out.println("任务起点为:" + task.get(0) +"  任务终点为:"+task.get(1));
                long time = res.getArg2();
                averageTime += time;
                maxTime = Math.max(maxTime, time);
                System.out.println("路径长度: " + res.getArg1().getShortestPath().size());
                System.out.println("寻路时间: " + res.getArg2());
            } catch (NoPathFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("平均时间: " + (double)(averageTime/taskNum));
        System.out.println("最长时间: " + maxTime);
    }
}
