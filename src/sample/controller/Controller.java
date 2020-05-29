package sample.controller;








import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
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
				myModel.setYStart(MouseEvent.getY());
			}else {
				
				Point2D currentPoint = new Point2D(MouseEvent.getX(),MouseEvent.getY());
				Shape selected = null;
				for(Shape s : Model.getShape()) {
					if(s.contains(currentPoint)) {
						selected = s;
					}
				}
				selected.getStrokeDashArray().add(2d);
				selected.setFill(myModel.getColor());
				selected.getStrokeDashArray().add(0d);
				
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
				Point2D currentPoint = new Point2D(MouseEvent.getX(),MouseEvent.getY());
				Shape selected = null;
				for(Shape s : Model.getShape()) {
					if(s.contains(currentPoint)) {
						selected = s;
					}
				}
				/*int shapeIndex = Model.getShape().indexOf(selected);
				Model.getShape().remove(selected);
				
				Class<? extends Shape> selectedClass = selected.getClass();*/
				/*switch(selectedClass.toString()){
				
				
				}*/
				
				
				
			}
			default : {
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
			}else {
				
			}

		});
		
		
		colorPicker.setOnAction(event ->{
			myModel.setColor(colorPicker.getValue());
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
}
