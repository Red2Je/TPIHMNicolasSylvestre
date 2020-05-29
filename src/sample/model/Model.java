package sample.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import sample.controller.Controller;

public class Model {

	private String currentShape;
	private Color shapeColor;
	private static ArrayList<Shape> shapeList = new ArrayList<>();
	private RadioButton currentSelectedButton;
	private double xStart;
	private double yStart;
	private double xEnd;
	private double yEnd;
	public Model() {
		this.shapeColor = Color.WHITE;
	}
	
	
	public void changeSelectedRadioButton(RadioButton button) {
		this.currentSelectedButton = button;
		this.changeShape();
	}
	
	public void changeShape() {
		switch(currentSelectedButton.getText()) {
		case "Ellipse" : this.currentShape = "Ellipse";break;
		case "Rectangle" : this.currentShape = "Rectangle";break;
		case "Line" : this.currentShape = "Line";break;
		case "Select/Move" : this.currentShape = null;break;
		}
	}
	
	
	public void setXStart(double xStart) {
		this.xStart = xStart;
	}
	
	public void setYStart(double yStart) {
		this.yStart = yStart;
	}
	
	public void setXEnd(double xEnd) {
		this.xEnd = xEnd;
	}
	
	public void setYEnd(double yEnd) {
		this.yEnd = yEnd;
	}
	
	public double getXStart() {
		return(this.xStart);
	}
	public double getYStart() {
		return(this.yStart);
	}
	public double getXEnd() {
		return(this.xEnd);
	}
	public double getYEnd() {
		return(this.yEnd);
	}
	
	public Color getColor() {
		return(this.shapeColor);
	}
	
	public void setColor(Color color) {
		this.shapeColor = color;
	}
	
	public String getCurrentButton() {
		return(this.currentShape);
	}
	
	
	public static void addShape(Shape shape) {
		shapeList.add(shape);
	}
	
	public static ArrayList<Shape> getShape() {
		return(shapeList);
	}
	
	
	
	

}
