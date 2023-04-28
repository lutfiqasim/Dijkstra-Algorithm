
public class Edge<T> implements Comparable<Edge> {
	private T connectedVertex;// Neighbour node
	private double weight;
	private static int index = 0;

	public Edge(T v, double w) {
		this.connectedVertex = v;
		this.weight = w;
		index++;
	}

	public T getConnectedVertex() {
		return connectedVertex;
	}

	public void setConnectedVertex(T connectedVertex) {
		this.connectedVertex = connectedVertex;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return "(" + connectedVertex + ";" + weight + ":In kilometers)\n";
	}

	@Override
	public int compareTo(Edge o) {
		return Double.compare(this.weight, o.getWeight());
	}

}
