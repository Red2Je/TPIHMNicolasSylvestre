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


	private Color shapeColor;//la couleur de notre shape
	private static ArrayList<Shape> shapeList = new ArrayList<>(); //la liste statique de toutes les shapes à dessiner
	private double xStart;//la coordonnée en x de départ de la forme
	private double yStart;//celle en y
	private double xEnd;//la coordonnée de fin de la forme
	private double yEnd;//celle en y
	public Model() {//le constructeur qui configure la couleur
		this.shapeColor = Color.WHITE;
	}
	
	

	

	
	//des getters et setters
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
	

	
	//la méthode d'ajout d'une shape dans la liste des shape à dessinner
	public static void addShape(Shape shape) {
		shapeList.add(shape);
	}
	//la méthode de get des shapes dans la liste des shapes à dessiner
	public static ArrayList<Shape> getShape() {
		return(shapeList);
	}
	
	
	
	

}
