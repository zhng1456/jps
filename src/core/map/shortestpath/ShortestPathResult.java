package core.map.shortestpath;

import core.util.Tuple2;
import core.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains the result of a shortest path search to give it back to the caller.
 *
 * @author Patrick Loka, Danny Rademacher
 * @version 1.0
 * @since 1.0
 */
public class ShortestPathResult {

    private Vector start, goal;
    private Collection<Vector> openList;
    private Map<Vector, Vector> pathPredecessors;
    private double cost;
    private List<Tuple2<Vector, Boolean>> path = new LinkedList<>();
    /**
     * Init a ShortestPathResult containing all selected data during he search.
     *
     * @param start
     *            startpoint of the search.
     * @param goal
     *            goalpoint of the search.
     * @param openList
     *            explored but not visited points from the priority queue.
     * @param pathPredecessors
     *            canculated predecessors for every visited point
     * @param cost
     *            costs for the shortest path
     * @since 1.0
     */
    public ShortestPathResult(Vector start, Vector goal, Collection<Vector> openList,
            Map<Vector, Vector> pathPredecessors, double cost) {
        this.start = start;
        this.goal = goal;
        this.openList = openList;
        this.pathPredecessors = pathPredecessors;
        this.cost = cost;
    }

    /**
     * Returns the start point of the ShortestPath.
     *
     * @return startpoint.
     * @since 1.0
     */
    public Vector getStart() {
        return this.start;
    }

    /**
     * Returns the goalpoint of the ShortestPath.
     *
     * @return goalpoint.
     * @since 1.0
     */
    public Vector getGoal() {
        return this.goal;
    }

    /**
     * Returns the shortest path.
     *
     * @return Set of all points containing to the shortest path and the
     *         information if the point was added to the openList during the
     *         search.
     * @since 1.0
     */
    public List<Tuple2<Vector, Boolean>> getShortestPath() {
        
        Vector current = goal;
        do {
            Vector next = pathPredecessors.get(current);
            if(next == null) System.out.println(current + "ǰ1����Ϊnull");
            Vector direction = current.getDirectionTo(next);
            Boolean flag = true;
            while (!current.equals(next)) {
                path.add(0, new Tuple2<>(current, flag));
                current = current.add(direction);
                flag = false;
            }
        } while (!current.equals(start));
        path.add(0, new Tuple2<>(start, true));
        // path.add(new Tuple2<>(current, true));
        return path;
    }

    /**
     * Returns the closed list.
     *
     * @return Set of all visited points during the search.
     * @since 1.0
     */
    public Collection<Vector> getClosedList() {
        return pathPredecessors.keySet();
    }

    /**
     * Returns the openList.
     *
     * @return Set of all explored but not visited points during the search.
     * @since 1.0
     */
    public Collection<Vector> getOpenList() {
        return this.openList.stream().filter(p -> !pathPredecessors.keySet().contains(p))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the cost of the shortest path.
     *
     * @return cost of the shortest path.
     * @since 1.0
     */
    public double getCost() {
        return this.cost;
    }
    // ����ת�����
    public int calcTurnTime(){
        int res = 0;
        int size = path.size();
        for(int i = 1;i < size -1;i++){
            Vector cur = path.get(i).getArg1();
            Vector pre = path.get(i-1).getArg1();
            Vector next = path.get(i+1).getArg1();
            // �ж��Ƿ�ת��
            // ��һ������
            if((pre.getX() == cur.getX() && cur.getX() == next.getX()) ||
                    (pre.getY() == cur.getY() && cur.getY() == next.getY())){
                
            }
            else{
                res++;
            }
        }
        return res;
    }
}
