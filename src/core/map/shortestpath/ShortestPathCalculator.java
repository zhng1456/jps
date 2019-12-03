package core.map.shortestpath;

import core.exception.NoPathFoundException;
import core.map.MapFacade;
import core.map.heuristic.Heuristic;
import core.map.movingrule.MovingRule;
import core.util.Tuple2;
import core.util.Tuple3;
import core.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ShortestPathCalculator provides a way to find a shortest path on a grid map
 * considering a preprocessing and a pruning strategy.
 *
 * @author Patrick Loka
 * @version 1.0
 * @since 1.0
 */
abstract class ShortestPathCalculator implements ExploreStrategy {

    private ShortestPathPreprocessing preprocessing = null;
    private ShortestPathPruning pruning = null;

    /**
     * Init a ShortestPathCalculator object with a specific preprocessing and
     * pruning strategy.
     *
     * @param preprocessing
     *            ShortestPathPreprocessing Impl.
     * @param pruning
     *            ShortestPathPruning Impl.
     * @since 1.0
     */
    protected ShortestPathCalculator(ShortestPathPreprocessing preprocessing, ShortestPathPruning pruning) {
        this.preprocessing = preprocessing;
        this.pruning = pruning;
    }

    /* ------- Preprocessing ------- */

    /**
     * Does all the preprocessing the preprocessing and pruning attributes need.
     *
     * @param map
     *            given grid map for on which the shortest path seach should
     *            run.
     * @param movingRule
     *            allowed movements on the map.
     * @since 1.0
     */
    protected void doPreprocessing(MapFacade map, MovingRule movingRule) {
        this.preprocessing.doPreprocessing(map, movingRule);
        this.pruning.doPreprocessing(map, movingRule);
    }

    /**
     * Decides whether a candidate is pruned or not considering the pruning
     * attribute.
     *
     * @param candidate
     *            candidate for pruning.
     * @param direction
     *            incoming direction of candidate.
     * @param goal
     *            goal of the shortest path search
     * @return true, if the given candidate should be pruned, else false
     * @since 1.0
     */
    protected boolean prune(Vector candidate, Vector direction, Vector goal) {
        return pruning.prune(candidate, direction, goal);
    }

    /* ------- ShortestPathCalculator ------- */

    /**
     * 这是一个A*的基本框架,用了java流式集合来操作
     *
     * @param map
     *            grid map.
     * @param start
     *            startpoint on the map.
     * @param goal
     *            goalpoint on the map.
     * @param heuristic
     *            heuristic to estimate the distance between two points. This
     *            influences the order in which points are processed by
     *            exploring the map.
     * @param movingRule
     *            allowed movements on the map.
     * @return The Shortest Path, the processed points and the explored but not
     *         processed points als well as the costs of the path.
     * @throws NoPathFoundException
     *             Thrown if there is no legal way between start and goal. This
     *             is also the case, iff one of the points is not passable.
     * @since 1.0
     */
    protected ShortestPathResult findShortestPath(MapFacade map, Vector start, Vector goal, Heuristic heuristic,
            MovingRule movingRule) throws NoPathFoundException {
        if (!map.isPassable(start) || !map.isPassable(goal))
            throw new NoPathFoundException(start, goal);
       // 用了map来存储前置节点
        Map<Vector, Vector> pathPredecessors = new HashMap<>();
       // open表
        PriorityQueue<Tuple3<Vector, Vector, Tuple2<Double, Double>>> openList = new PriorityQueue<>((p, q) -> {
            if (p.getArg3().getArg1() + p.getArg3().getArg2() > q.getArg3().getArg1() + q.getArg3().getArg2())
                return 1;
//            if (p.getArg3().getArg2() > q.getArg3().getArg2())
//                return 1;
            return -1;
        });
        // 起点加入open表
        openList.add(new Tuple3<>(start, null, new Tuple2<>(.0, heuristic.estimateDistance(start, goal))));
        while (!openList.isEmpty()) {
            Tuple3<Vector, Vector, Tuple2<Double, Double>> currentPath = openList.poll();
            // 当前点(三元组的第一个参数)
            Vector currentPoint = currentPath.getArg1();
            // 当前点的前置点(三元组的第二个参数)
            Vector currentPredecessor = currentPath.getArg2();
            // 距离,f = g + h中的g
            double pathDistance = currentPath.getArg3().getArg1();
            if (pathPredecessors.get(currentPoint) != null)
                continue;
            // 设置前置点
            pathPredecessors.put(currentPoint, currentPredecessor);
            if (currentPoint.equals(goal)) {
                return new ShortestPathResult(start, goal,
                        openList.stream().map(entry -> entry.getArg1()).collect(Collectors.toList()), pathPredecessors,
                        pathDistance);
            }
            // 去找当前点的下一个点
            openList.addAll(getOpenListCandidates(map, currentPoint, currentPredecessor, goal, movingRule, pathPredecessors).stream()
                    .filter(candidate -> pathPredecessors.get(candidate.getArg1()) == null)
                    .map(candidate -> new Tuple3<>(candidate.getArg1(), currentPoint,
                            new Tuple2<>(pathDistance + candidate.getArg2(),
                                    heuristic.estimateDistance(candidate.getArg1(), goal))))
                    .collect(Collectors.toList()));
        }
        throw new NoPathFoundException(start, goal);
    }

    /**
     * 从当前点，找下一个点的操作,不同的算法，会有不同的实现 JPS会去算跳点,JPS plus会直接取缓存的跳点
     * 
     * @param map
     * @param currentPoint
     * @param predecessor
     * @param goal
     * @param movingRule
     * @return
     */

    private Collection<Tuple2<Vector, Double>> getOpenListCandidates(MapFacade map, Vector currentPoint,
            Vector predecessor, Vector goal, MovingRule movingRule, Map<Vector, Vector> pathPredecessors) {
        Collection<Vector> directions = getDirectionsStrategy(map, currentPoint, predecessor, movingRule);
        Collection<Tuple2<Vector, Double>> candidates = new ArrayList<>();
        for (Vector dir : directions) {
            // 各个算法的exploreStrategy不同
            Tuple2<Vector, Double> candidate = exploreStrategy(map, currentPoint, dir, 0.0, goal, movingRule, candidates,pathPredecessors);
            if (candidate != null && !prune(candidate.getArg1(), dir, goal))
                candidates.add(candidate);
        }
        return candidates;
    }
}