package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.Tuple2;
import core.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

/**
 * JPS implementation of ShortestPathCalculator
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
class JPSShortestPathCalculator extends ShortestPathCalculator {

    protected JPSShortestPathCalculator(ShortestPathPruning pruning) {
        super(new NoShortestPathPreprocessing(), pruning);
    }

    @Override
    public Collection<Vector> getDirectionsStrategy(MapFacade map, Vector currentPoint, Vector predecessor,
            MovingRule movingRule) {
        if (predecessor != null) {
            Collection<Vector> directions = new ArrayList<>();
            Vector direction = predecessor.getDirectionTo(currentPoint);
            directions.add(direction);
            directions.addAll(movingRule.getForcedDirections(map, currentPoint, direction));
            directions.addAll(movingRule.getSubordinatedDirections(direction));
            return directions;
        } else {
            return movingRule.getAllDirections();
        }
    }

    /**
     * JPS算法使用,递归找下一个点
     */
    @Override
    public Tuple2<Vector, Double> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost,
            Vector goal, MovingRule movingRule) {
        // 沿着direction到了下一个点candidate
        Vector candidate = currentPoint.add(direction);
        // 不可达则直接返回
        if (!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction))
            return null;
        // 新的cost，即g值;这里就是走了多少步
        cost += Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()));
        // 到达了终点,返回candiate(认为终点也是跳点)
        if (candidate.equals(goal))
            return new Tuple2<>(candidate, cost);
        // 根据JPS算法的原理,有强制邻居，则这个点是跳点
        if (movingRule.getForcedDirections(map, candidate, direction).size() > 0)
            return new Tuple2<>(candidate, cost);
        // 求子方向;原来是水平jump，则要转换为向上下2个方向Jump
        for (Vector subordinatedDirection : movingRule.getSubordinatedDirections(direction)) {
            if (exploreStrategy(map, candidate, subordinatedDirection, cost, goal, movingRule) != null)
                return new Tuple2<>(candidate, cost);
        }
        // 递归
        return exploreStrategy(map, candidate, direction, cost, goal, movingRule);
    }
}
