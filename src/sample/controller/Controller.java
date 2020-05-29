package sample.controller;








import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import sample.model.Model;

public class Controller {

	private Model myModel;
	private RadioButton currentButton;

	
	@FXML
	private RadioButton SelectRadio;
	
	@FXML
	private ToggleGroup group1;
	
	@FXML
	private RadioButton EllipseRadio;
	
	@FXML
	private RadioButton RectangleRadio;
	
	@FXML
	private RadioButton LineRadio;
	
	@FXML
	private ColorPicker colorPicker;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private Button cloneButton;
	
	@FXML
    private AnchorPane drawPane;
	
	private boolean isMouseClicked = false;
	private Shape selectedShape;
	private double xT;
	private double yT;
	private int index;
	public Controller() {
		this.currentButton = SelectRadio;
		this.myModel = new Model();
		
	}

	public void initialize() {
		
		//we handle the buttons events
		EllipseRadio.setOnAction(event ->{
			myModel.changeSelectedRadioButton(EllipseRadio);
			this.currentButton = EllipseRadio;
		});
		
		RectangleRadio.setOnAction(event ->{
			myModel.changeSelectedRadioButton(RectangleRadio);
			this.currentButton = RectangleRadio;
		});
		LineRadio.setOnAction(event ->{
			myModel.changeSelectedRadioButton(LineRadio);
			this.currentButton = LineRadio;
		});
		SelectRadio.setOnAction(event ->{
			myModel.changeSelectedRadioButton(SelectRadio);
			this.currentButton = SelectRadio;
		});
		
		//we handle the mouse events
		drawPane.setOnMousePressed(MouseEvent ->{
			if(currentButton != SelectRadio) {
				myModel.setXStart(MouseEvent.getX());
				myModel.setYStart(MouseEvent.getY());}

			
		});
		
		drawPane.setOnMouseClicked(MouseEvent->{
			if(currentButton == SelectRadio) {
				Point2D currentPoint = new Point2D(MouseEvent.getX(),MouseEvent.getY());
				Shape selected = null;
				for(Shape s : Model.getShape()) {
					if(s.contains(currentPoint)) {
						selected = s;
					}
				}
				if(selected != null) {
					if(!isMouseClicked) {
						this.selectedShape = selected;
						this.isMouseClicked = true;
						this.index = Model.getShape().indexOf(selected);
						selected.getStrokeDashArray().add(2d);
						this.xT = MouseEvent.getX();
						this.yT = MouseEvent.getY();

					}else {
						this.selectedShape.getStrokeDashArray().add(0d);
						this.selectedShape = null;
						this.isMouseClicked = false;
						this.index = 0;
						this.xT = 0;
						this.yT = 0;
					}
				}
				
			}
		});
		
		drawPane.setOnMouseDragged(MouseEvent ->{
			switch(currentButton.getText()){
			case "Line" : {
				myModel.setXEnd(MouseEvent.getX());
				myModel.setYEnd(MouseEvent.getY());
				break;
			}
			case "Select/Move" :{
				if(isMouseClicked) {
					this.selectedShape.setTranslateX(MouseEvent.getX()-this.xT);
					this.selectedShape.setTranslateY(MouseEvent.getY()-this.yT);
				}
				
				
				
				
			}
			case"Ellipse" : {
				myModel.setXEnd(MouseEvent.getX());
				myModel.setYEnd(MouseEvent.getY());
				break;
			}
			case "Rectangle" : {
				myModel.setXEnd(MouseEvent.getX());
				myModel.setYEnd(MouseEvent.getY());
				break;
			}
			
			}
			
		});
		
		drawPane.setOnMouseReleased(MouseEvent ->{
			if(currentButton != SelectRadio) {
				this.newShape(Color.BLACK, myModel.getColor());
				this.draw();
				myModel.setXEnd(0);
				myModel.setYEnd(0);
				myModel.setXStart(0);
				myModel.setYStart(0);
			}

		});
		
		
		colorPicker.setOnAction(event ->{
			myModel.setColor(colorPicker.getValue());
			if(isMouseClicked) {
				this.updateSelectedShape(colorPicker.getValue());
			}
			
			
		});
		
		
		//handle delete button
		deleteButton.setOnAction(event ->{
			if(this.currentButton == SelectRadio) {
				Model.getShape().remove(this.selectedShape);
				this.isMouseClicked = false;
				this.index = 0;
				this.xT = 0;
				this.yT = 0;
				this.draw();
			}
		});
		
		//handle clone button
		cloneButton.setOnAction(event ->{
			try {
				String className = this.selectedShape.getClass().toString();
				String arrayShape = Model.getShape().toString();
				String[] splitted = arrayShape.split(",");
				System.out.println(splitted[1]+"\n");
				if(className.contains("Ellipse")) {
					Double x = Double.parseDouble(splitted[0].replaceAll("[^0-9]", ""))/10;
					Double y = Double.parseDouble(splitted[1].replaceAll("[^0-9]", ""))/10;
					Double radiusX = Double.parseDouble(splitted[2].replaceAll("[^0-9]", ""))/10;
					Double radiusY = Double.parseDouble(splitted[3].replaceAll("[^0-9]", ""))/10;
					
					Ellipse ell = new Ellipse();
					ell.setCenterX(x+10);
					ell.setCenterY(y+10);
					ell.setRadiusX(radiusX);
					ell.setRadiusY(radiusY);
					ell.setFill(Color.WHITE);
					Model.addShape(ell);
					this.draw();
				}
				if(className.contains("Rectangle")) {
					Double x = Double.parseDouble(splitted[0].replaceAll("[^0-9]", ""))/10;
					Double y = Double.parseDouble(splitted[1].replaceAll("[^0-9]", ""))/10;
					Double width = Double.parseDouble(splitted[2].replaceAll("[^0-9]", ""))/10;
					Double height = Double.parseDouble(splitted[3].replaceAll("[^0-9]", ""))/10;
					Rectangle rect = new Rectangle();
					rect.setX(x+10);
					rect.setY(y+10);
					rect.setWidth(width);
					rect.setHeight(height);
					rect.setFill(Color.WHITE);
					Model.addShape(rect);
					this.draw();
					
				}
				if(className.contains("Line")) {
					Double x = Double.parseDouble(splitted[0].replaceAll("[^0-9]", ""))/10;
					Double y = Double.parseDouble(splitted[1].replaceAll("[^0-9]", ""))/10;
					Double endX = Double.parseDouble(splitted[2].replaceAll("[^0-9]", ""))/10;
					Double endY = Double.parseDouble(splitted[3].replaceAll("[^0-9]", ""))/10;
					Line line = new Line(x+10,y+10,endX+10,endY+10);
					line.setStroke(Color.WHITE);
					Model.addShape(line);
					this.draw();
				}
			}catch(Exception e) {
			}
			
				
			
			
		});
		
	}
	
	public RadioButton getCurrentButton() {
		return(this.currentButton);
	}
	
	public void newShape(Color strokeColor, Color fillColor) {

		switch(currentButton.getText()){
			case "Ellipse" :{
				Ellipse ell = new Ellipse();
				
				ell.setCenterX(myModel.getXStart());
				ell.setCenterY(myModel.getYStart());
				ell.setRadiusX(Math.min(Math.abs(myModel.getXEnd()-myModel.getXStart()),myModel.getXStart()));
				ell.setRadiusY(Math.min(Math.abs(myModel.getYEnd()-myModel.getYStart()),Math.abs(myModel.getYStart())));
				ell.setFill(fillColor);
				ell.setStroke(strokeColor);
				Model.addShape(ell);
				break;
			}
			case "Rectangle" :{
				Rectangle rect = new Rectangle();
				double xs = myModel.getXStart();
				double xe = myModel.getXEnd();
				double ys = myModel.getYStart();
				double ye = myModel.getYEnd();
				double deltaX = xe-xs;
				double deltaY = ye-ys;
				if(deltaX<0){
					rect.setX(xe);
					rect.setWidth(-deltaX);
				}else {
					rect.setX(xs);
					rect.setWidth(deltaX);
				}
				if(deltaY<0){
					rect.setY(ye);
					rect.setHeight(-deltaY);
				}else {
					rect.setY(ys);
					rect.setHeight(deltaY);
				}
				rect.setFill(fillColor);
				rect.setStroke(strokeColor);

				Model.addShape(rect);
				break;
				
			}
			case "Line" : {
				Line line = new Line();
				line.setStartX(myModel.getXStart());
				line.setStartY(myModel.getYStart());
				line.setEndX(Math.max(myModel.getXEnd(),0));
				line.setEndY(Math.max(myModel.getYEnd(),0));
				line.setStroke(fillColor);
				Model.addShape(line);
				break;
			}
		}
		
	}


	public void draw() {
		drawPane.getChildren().clear();
		System.out.println(Model.getShape().toString());
		drawPane.getChildren().addAll(Model.getShape());
		
	}
	
	public void updateSelectedShape(Color color) {
		this.selectedShape.setFill(color);
	}
}
