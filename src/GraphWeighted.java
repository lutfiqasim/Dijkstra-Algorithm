import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GraphWeighted<T> {
	Map<T, LinkedList<Edge<T>>> adjacent = new HashMap<>();
	static int i = 0;

	public GraphWeighted() {
	}

	public void addVertices(T vertices) {
		adjacent.putIfAbsent(vertices, new LinkedList<>());
		((Country) vertices).newCountry(this.i);
		i++;

	}

	// Add edges includes adding a node
	public void addEdge(T a, T b, double w) {
		adjacent.putIfAbsent(a, new LinkedList<>());// Adding a node
		adjacent.putIfAbsent(b, new LinkedList<>());
		Edge<T> edge1 = new Edge<>(b, w);
		adjacent.get(a).add(edge1);// Adding edge from a to b
	}

	// find edge between two nodes, T(n)=O(n), space =O(c),n=#of neighbors
	private Edge<T> findEdgeByVertex(T a, T b) {
		LinkedList<Edge<T>> neighbourEdges = adjacent.get(a);
		for (Edge<T> edge : neighbourEdges) {// Iterate through neighbours
			if (edge.getConnectedVertex().equals(b)) {
				return edge;//
			}
		}
		return null;// b isn't a neighbour of a
	}

	// Remove direct edge between two vertices Time=O(n), Space=O(c)
	// Directed graph so only remove edge from a to b
	public void removeEdge(T a, T b) {
		LinkedList<Edge<T>> neighbourEdge1 = adjacent.get(a);
		if (neighbourEdge1 == null)
			return;
		Edge<T> edgeFromAToB = findEdgeByVertex(a, b);
		if (edgeFromAToB != null)
			neighbourEdge1.remove(edgeFromAToB);
	}

	// Remove a vertices from directed graph
	// Includes removing all its edges
	// Time O(V), where V is number of vertices in graph
	public void removeVertics(T v) {
		for (T key : adjacent.keySet()) {// Traverse through keySets of map
			// Findes edge from current vertices to
			// the vertices we want to remove and remove it
			Edge<T> edge2 = findEdgeByVertex(key, v);
			if (edge2 != null)// if such an edge exists
				adjacent.get(key).remove(edge2);
		}
		// After removing all edges going to V
		// remove V
		adjacent.remove(v);
	}

	// Search a vertices by its key
	// Time O(c) space O(c) since i used a hashmap
	public boolean hasNode(T key) {
		return adjacent.containsKey(key);
	}

	// Check whether there is direct edge between two nodes
	// Time O(n)
	public boolean hasEdge(T a, T b) {
		Edge<T> edge1 = findEdgeByVertex(a, b);
		return edge1 != null;
	}

	// Print graph as hashmap Time O(V+E)
	@Override
	public String toString() {
		String s = "Distance in kilometers";
		for (T key : adjacent.keySet()) {
			s += key + "," + adjacent.get(key) + "\n";
		}
		return s;
	}

}
