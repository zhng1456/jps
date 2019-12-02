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
 * 测试一下各个算法的调用方法,使用空地图,无障碍;500x500地图,从左上角跑到右下角
 * 
 * @author Durant
 *
 */
public class TestAlgorithm {
    private MapController controller = new MapController();

    /**
     * 初始化，设置算法以及规则等
     * 
     * @throws MapInitialisationException
     */
    @Before
    public void init() throws MapInitialisationException {
        controller.setEuclideanHeuristic();// 使用欧式距离
        controller.setEmptyMap(new Vector(500, 500));// 在500x500的地图上
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
