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
     * JPS�㷨ʹ��,�ݹ�����һ����
     */
    @Override
    public Tuple2<Vector, Double> exploreStrategy(MapFacade map, Vector currentPoint, Vector direction, Double cost,
            Vector goal, MovingRule movingRule) {
        // ����direction������һ����candidate
        Vector candidate = currentPoint.add(direction);
        // ���ɴ���ֱ�ӷ���
        if (!map.isPassable(candidate) || movingRule.isCornerCut(map, currentPoint, direction))
            return null;
        // �µ�cost����gֵ;����������˶��ٲ�
        cost += Math.sqrt(Math.abs(direction.getX()) + Math.abs(direction.getY()));
        // �������յ�,����candiate(��Ϊ�յ�Ҳ������)
        if (candidate.equals(goal))
            return new Tuple2<>(candidate, cost);
        // ����JPS�㷨��ԭ��,��ǿ���ھӣ��������������
        if (movingRule.getForcedDirections(map, candidate, direction).size() > 0)
            return new Tuple2<>(candidate, cost);
        // ���ӷ���;ԭ����ˮƽjump����Ҫת��Ϊ������2������Jump
        for (Vector subordinatedDirection : movingRule.getSubordinatedDirections(direction)) {
            if (exploreStrategy(map, candidate, subordinatedDirection, cost, goal, movingRule) != null)
                return new Tuple2<>(candidate, cost);
        }
        // �ݹ�
        return exploreStrategy(map, candidate, direction, cost, goal, movingRule);
    }
}
