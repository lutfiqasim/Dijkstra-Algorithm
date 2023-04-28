import java.util.LinkedList;
import java.util.List;

public class TableEntry {
//	List<Country> header = new LinkedList<>();
	boolean known;
	double distance;
	Country path;
	public double getDistance() {
		double dis = (int) (distance * 100) / 100.0;
		return dis;
	}

	@Override
	public String toString() {
		return path + " ";
	}

}
