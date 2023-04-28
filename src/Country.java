import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Country {
	private double lattitude;
	private double longtidue;
	private String name;
	Circle c;
	Tooltip toolTipTxt;
	private int i;
	Line l;

	public Country(String name, double lattitude, double longtidue) {
		this.name = name;
		this.lattitude = lattitude;
		this.longtidue = longtidue;
		createCircle();
	}

	public String getName() {
		return this.name;
	}

	public double getlongtidue() {
		return this.longtidue;
	}

	public double getlatitude() {
		return this.lattitude;
	}

	public int getNum() {
		return i;
	}

	public void newCountry(int num) {
		this.i = num;
	}

	private void createCircle() {
		l = new Line();
		l.toFront();
		l.setStroke(Color.BLACK);
		l.setStrokeWidth(2);
		c = new Circle(3);
		c.setFill(Color.RED);
		c.setTranslateZ(4);
		setXAndYProperty();
		l.setStartX(c.getTranslateX());
		l.setStartY(c.getTranslateY());
		Tooltip toolTipTxt = new Tooltip(this.name);
		// Setting the tool tip to the text field
		Tooltip.install(c, toolTipTxt);
		c.setOnMouseEntered(e -> {
			c.setRadius(10);
		});
		c.setOnMouseExited(e -> {
			c.setRadius(3);
		});

	}

	private void setXAndYProperty() {
		double xPosition = 0.0;
		double yPosition = 0.0;
		if (longtidue < 0 && lattitude > 0) {
			xPosition = (1200.0 / 2.0) + (this.longtidue * 3.3048);// Longtidue
			yPosition = (850.0 / 2.0) - (this.lattitude * 2.8811);// Latitude
		} else {
//			System.out.println(i++);
			xPosition = (1200.0 / 2.0) + (this.longtidue * 3.3048);// Longtidue : 3.3048
			yPosition = (850.0 / 2.0) - (this.lattitude * 2.3611);// Latitude 2.0104
		}
		c.setTranslateX(xPosition);
		c.setTranslateY(yPosition);
	}

	public String toString() {
//		return "[Country:" + this.name + ", " + "lattitude:" + this.lattitude + ", " + "longtidue:" + this.longtidue
//				+ "]";
		return this.name + ", ";
	}

}
