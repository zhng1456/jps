package core.util;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import core.map.Map;


public class TaskUtil {
    Random r = new Random();
    public List<List<Vector>> getRandomTask(Map mapData, int num){
        List<List<Vector>> res = new LinkedList<List<Vector>>();
        int xLength = mapData.getXDim();
        int yLength = mapData.getYDim();
        for(int i = 0;i < num;){
            List<Vector> task = new LinkedList<Vector>();
            //起点坐标
            int x1 = r.nextInt(xLength);
            int y1 = r.nextInt(yLength);
            //终点坐标
            int x2 = r.nextInt(xLength);
            int y2 = r.nextInt(yLength);
            // 起点终点相差足够大，这样才能起到测试效果
            if(mapData.isPassable(new Vector(x1,y1)) && mapData.isPassable(new Vector(x2,y2)) && Math.abs(x1-x2) + Math.abs(y1-y2) > xLength){
                task.add(new Vector(x1,y1));
                task.add(new Vector(x2,y2));
                res.add(task);
                i++;
            }
        }
        return res;
    }
}
