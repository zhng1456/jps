package core.map.shortestpath;

import core.map.MapFacade;
import core.map.movingrule.MovingRule;
import core.util.Tuple2;
import core.util.Tuple3;
import core.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * JPS implementation of PreCalculationShortestPathPreprocessing
 *
 * @author Parick Loka
 * @version 1.0
 * @since 1.0
 */
class JPSPreCalculationShortestPathPreprocessing extends PreCalculationShortestPathPreprocessing {

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

    @Override
    public Tuple3<Vector, Double, Boolean> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction,
            Double cost, Vector goal, MovingRule movingRule) {
        // 避免重复计算，已存储的跳点直接返回
        if (getPreprocessing(currentPoint, direction) != null) {
            Tuple3<Vector, Double, Boolean> preprocessedPoint = getPreprocessing(currentPoint, direction);
            return new Tuple3<>(preprocessedPoint.getArg1(), preprocessedPoint.getArg2() + cost,
                    preprocessedPoint.getArg3());
        }
        // 障碍
        Vector candidate = currentPoint.add(direction);
        if (!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction)) {
            return new Tuple3<>(currentPoint, cost, false);
        }
        
        Double stepCost = Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()));
        // 有强制邻居即是跳点
        if (movingRule.getForcedDirections(map, candidate, direction).size() > 0) {
            putPreprocessing(currentPoint, direction, new Tuple3<>(candidate, stepCost, true));
            return new Tuple3<>(candidate, cost + stepCost, true);
        }

        for (Vector subordinatedDirection : movingRule.getSubordinatedDirections(direction)) {
            if (exploreStrategy(map, candidate, subordinatedDirection, 1.0, goal, movingRule).getArg3()) {
                putPreprocessing(currentPoint, direction, new Tuple3<>(candidate, stepCost, true));
                return new Tuple3<>(candidate, cost + stepCost, true);
            }
        }
        // 向当前方向递归
        Tuple3<Vector, Double, Boolean> result = exploreStrategy(map, candidate, direction, cost + stepCost, goal,
                movingRule);
        putPreprocessing(currentPoint, direction,
                new Tuple3<>(result.getArg1(), result.getArg2() - cost, result.getArg3()));
        return result;
    }

    @Override
    public Tuple2<Vector, Double> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost,
            Vector goal, MovingRule movingRule, Collection<Tuple2<Vector, Double>> candidates,Map<Vector, Vector> pathPredecessors) {
        // 为了改进JPS而新增
        return exploreStrategy(map,currentPoint,direction,cost,goal,movingRule);
    }
}
