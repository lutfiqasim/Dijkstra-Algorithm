import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Driver extends Application {
	final static Font font3 = Font.font("Times New Roman", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 20);
	final static Font font4 = Font.font("Times New Roman", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 12);
	private static BorderPane border = new BorderPane();
	private static TextArea txtArea_result = new TextArea();
	private static TextArea txtArea_path = new TextArea();
	private static GraphWeighted<Country> graph = new GraphWeighted<>();
	private static Map<String, Country> countriesMap = new HashMap<>();
	private static byte selected = 0;
	static ComboBox<String> cmb_start;
	static ComboBox<String> cmb_target;
	static TableEntry[] table;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.UTILITY);
		InputStream photoStream = new FileInputStream("Mercator_projection_Square.jpeg");
		table = new TableEntry[graph.adjacent.size() + 1];
		initializeTable();
		Image worldMap = new Image(photoStream);
		ImageView view = new ImageView();
		view.setImage(worldMap);
		view.setFitWidth(1200);
		view.setFitHeight(850);
		border.getChildren().add(view);
		Label lbl_start = new Label("Start: ");
		lbl_start.setFont(font3);
		cmb_start = new ComboBox<>();
		cmb_start.setPromptText("Select your Start");
		HBox hstart = new HBox(lbl_start, cmb_start);
		hstart.setSpacing(20);
		Label lbl_target = new Label("Target:");
		lbl_target.setFont(font3);
		cmb_target = new ComboBox<>();
		cmb_target.setPromptText("Select Your target");
		for (Entry<String, Country> c : countriesMap.entrySet()) {
			cmb_start.getItems().add(c.getValue().getName());
			cmb_target.getItems().add(c.getValue().getName());
			c.getValue().c.setOnMouseClicked(e -> {
				if (selected == 0) {
					cmb_start.setValue(c.getValue().getName());
					selected++;
				} else {
					cmb_target.setValue(c.getValue().getName());
					selected = 0;
				}
			});
		}
		HBox htarget = new HBox(lbl_target, cmb_target);
		htarget.setSpacing(20);
		Button btnStart = new Button("Start");
		btnStart.setFont(font3);
		HBox btn_box = new HBox(btnStart);
		btn_box.setSpacing(20);
		btn_box.setAlignment(Pos.CENTER);
		Label lbl_result = new Label("Result");
		lbl_result.setFont(font3);
		txtArea_result.setPrefRowCount(10);
		txtArea_result.setPrefColumnCount(20);
		txtArea_result.setFont(font4);
		HBox hArea = new HBox(lbl_result, txtArea_result);
		hArea.setSpacing(5);
		Label lbl_path = new Label("Path:");
		lbl_path.setFont(font3);
		txtArea_path.setPrefRowCount(5);
		txtArea_path.setPrefColumnCount(20);
		txtArea_path.setFont(font4);
		HBox hPath = new HBox(lbl_path, txtArea_path);
		hPath.setSpacing(5);
		VBox v = new VBox(hstart, htarget, btn_box, hArea, hPath);
		v.setSpacing(50);
		border.setRight(v);
		border.setPadding(new Insets(40, 20, 20, 20));
		for (Entry<String, Country> c : countriesMap.entrySet()) {
			border.getChildren().add(c.getValue().c);
			border.getChildren().add(c.getValue().l);
			c.getValue().l.setVisible(false);
		}
		border.setStyle("-fx-background-color:SkyBlue;");
		Scene scene = new Scene(border, 1800, 750);
		// Get x and y position of a scene
//		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				System.out.println(event.getSceneX());
//				System.out.println(event.getSceneY());
//			}
//		});
		primaryStage.setFullScreen(true);
		primaryStage.setScene(scene);
//		primaryStage.setResizable(false);
		primaryStage.show();
		btnStart.setOnAction(e -> {
			try {
				OnStart();
			} catch (Exception nullExc) {
				nullExc.printStackTrace();
				warning_Message("Enter starting point and end point");
			}
		});
	}

	private void OnStart() {
		String start = cmb_start.getValue();
		String end = cmb_target.getValue();
		Dijkstra(countriesMap.get(start), countriesMap.get(end));
		StringBuilder path = new StringBuilder("");
		printPath(countriesMap.get(end), path);
//		printPath(countriesMap.get(end), "");
		txtArea_path.setText(path.toString());
		txtArea_result.setText("Distance to go from " + start + " to " + end + "\n="
				+ table[countriesMap.get(end).getNum()].getDistance() + "km");		
	}

	private void printPath(Country start, StringBuilder s) {
		if (table[start.getNum()].path != null) {
			table[start.getNum()].path.l.setEndX(start.c.getTranslateX());
			table[start.getNum()].path.l.setEndY(start.c.getTranslateY());
			table[start.getNum()].path.l.setVisible(true);
			table[start.getNum()].path.c.setFill(Color.BLUE);
//			border.getChildren().add(table[start.getNum()].path.l);
			printPath(table[start.getNum()].path, s);
			s.append("to :");
		}
		s.append(start + " Distance: " + table[start.getNum()].getDistance() + " km\n");
	}

	private void initializeTable() {
		for (int i = 0; i < table.length; i++) {
			table[i] = new TableEntry();
			table[i].known = false;
			table[i].path = null;
			table[i].distance = Double.MAX_VALUE;
		}
	}

	private void Dijkstra(Country start, Country end) {
		for (int i = 0; i < table.length; i++) {
//			table[i] = new TableEntry();
			table[i].known = false;
			if (table[i].path != null) {
				table[i].path.l.setVisible(false);
				table[i].path.c.setFill(Color.RED);
			}
			table[i].path = null;
			table[i].distance = Double.MAX_VALUE;
		}
		PriorityQueue<Edge> pq = new PriorityQueue<>();
		pq.add(new Edge(start, 0));
		table[start.getNum()].distance = 0;
		while (!pq.isEmpty()) {
			Country u = (Country) pq.poll().getConnectedVertex();
//			System.out.println(u);
			if (table[u.getNum()].known) {
				continue;
			}
			table[u.getNum()].known = true;
			if (table[end.getNum()].known) {
//				System.out.println("Break");
				break;
			}
			e_Neigbours(u, table, pq);
		}

	}

	private void e_Neigbours(Country u, TableEntry[] table, PriorityQueue pq) {
		double edgeDis = -1;
		double newDis = -1;
		// All neighbours of v
		for (Edge<Country> c : graph.adjacent.get(u)) {
			// if current vertix hasn't been processed
			if (!table[c.getConnectedVertex().getNum()].known) {
				edgeDis = c.getWeight();
				newDis = table[u.getNum()].distance + edgeDis;
				// if new distance is cheaper
				if (newDis < table[c.getConnectedVertex().getNum()].distance) {
					table[c.getConnectedVertex().getNum()].distance = newDis;
					table[c.getConnectedVertex().getNum()].path = u;
				}

				// Add current node to the queue
				pq.add(new Edge(c.getConnectedVertex(), table[c.getConnectedVertex().getNum()].distance));
			}
		}
	}

	public static void main(String[] args) {
		try {
			readData("worldData.txt");
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
//			warning_Message(e.toString());
		}
	}

	private static void readData(String fileName) {
		File stdFile = new File(fileName);
		try (Scanner input = new Scanner(stdFile)) {
			String numOfData = input.nextLine();
			String[] str = numOfData.split(" ");
			int countries = Integer.valueOf(str[0]);
			int edges = Integer.valueOf(str[1]);
			int countriesRead = 0;
			int edgesRead = 0;
			while (input.hasNext()) {
				if (countriesRead < countries) {
					String countryData = input.nextLine();
					String[] tok = countryData.split(" ");
					Country coun = new Country(tok[0].strip(), Double.parseDouble(tok[1].strip()),
							Double.parseDouble(tok[2].strip()));
					graph.addVertices(coun);
					countriesMap.put(coun.getName(), coun);
					countriesRead++;
				} else if (edgesRead < edges) {
					String edgesData = input.nextLine();
					String[] tok = edgesData.split(" ");
//					System.out.println("1-" + countriesMap.get(tok[0]) + "2-" + countriesMap.get(tok[1]));
					graph.addEdge(countriesMap.get(tok[0]), countriesMap.get(tok[1]),
							calculateDistance(countriesMap.get(tok[0]), countriesMap.get(tok[1])));
					edgesRead++;
				}
			}
		} catch (FileNotFoundException e) {
			warning_Message("Error: No Data Was Entered");
		} catch (Exception e) {
			warning_Message("Formatting error");
		}
	}

	// to get distance between two countries
	private static double calculateDistance(Country c1, Country c2) {
		// Convert values to radians
		double lon1 = Math.toRadians(c1.getlongtidue());
		double lat1 = Math.toRadians(c1.getlatitude());
		double lon2 = Math.toRadians(c2.getlongtidue());
		double lat2 = Math.toRadians(c2.getlatitude());
		// Haversine Formula which calculates distances
		double dlon = lon2 - lon1;
		double dlat = lat2 - lat1;
		double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		double r = 6371;// radius of earth in Kilometers : 3956 if asked to be in miles
		// return result
		return (c * r);
	}

	public static void warning_Message(String x) {
		Alert alert = new Alert(AlertType.NONE);
		alert.setAlertType(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setContentText(x);
		alert.show();
	}

}
