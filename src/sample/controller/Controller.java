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

	private Model myModel;//le mod�le li� au controller
	private RadioButton currentButton;//le bouton s�l�ctionn� par d�faut

	
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
	
	private boolean isMouseClicked = false;// un indicateur de si un �l�ment � �t� s�l�ctionner
	private Shape selectedShape;//la forme s�l�ctionn�e
	private double xT;//l'endroit du clic o� l'on a s�l�ctionn� la forme
	private double yT;
	private int index;//l'index de la forme s�l�ctionn�e dans l'ArrayList de model : shapelist
	public Controller() {
		this.currentButton = SelectRadio;//le bouton par d�faut est celui de s�l�ction
		this.myModel = new Model();
		
	}

	public void initialize() {
		
		//On s'occupe des �v�nements sur les boutons : ils s'agit juste de hanger le bouton actif dans le mod�le et dans le controlleur
		EllipseRadio.setOnAction(event ->{
			this.currentButton = EllipseRadio;//et dans le controlleur
		});
		
		RectangleRadio.setOnAction(event ->{
			this.currentButton = RectangleRadio;
		});
		LineRadio.setOnAction(event ->{
			this.currentButton = LineRadio;
		});
		SelectRadio.setOnAction(event ->{
			this.currentButton = SelectRadio;
		});
		
		//si on appuie sur la souris dans la zone de dessin, avec une s�l�ction autre que le bouton de s�l�ction, on d�finit le d�but d'une nouvelle forme
		drawPane.setOnMousePressed(MouseEvent ->{
			if(currentButton != SelectRadio) {
				myModel.setXStart(MouseEvent.getX());
				myModel.setYStart(MouseEvent.getY());}

			
		});
		//si le bouton a �t� enfonc� puis d�senfoncer, on d�termine si l'on doit s�lectionner une forme ou dessiner une nouvelle shape, et on s'�x�cute
		drawPane.setOnMouseClicked(MouseEvent->{
			if(currentButton == SelectRadio) {//cas ou la selection du bouton est "Select/Move"
				Point2D currentPoint = new Point2D(MouseEvent.getX(),MouseEvent.getY());//on r�cup�re les coordonn�es de la souris, que l'on transforme en point (pas n�cessaire, mais j'avais pas vu le polymorphisme de contains)
				Shape selected = null;//si rien n'est s�l�ctionn�, selectedShape vaudra Null
				for(Shape s : Model.getShape()) {//on parcours la liste des shapes et on d�termine si une contient notre s�l�ction
					if(s.contains(currentPoint)) {
						selected = s;
					}
				}
				if(selected != null) {//si on en trouve une : 
					if(!isMouseClicked) {//si on n'a pas encore de s�l�ction:
						this.selectedShape = selected;
						this.isMouseClicked = true;
						this.index = Model.getShape().indexOf(selected);//on retrouve l'index de la shape que l'on a s�l�ctionn�e
						selected.getStrokeDashArray().add(2d);//on met ses bords en pointill�s
						this.xT = MouseEvent.getX();//on d�finit l� o� l'on �tait quand on a cliqu� dessus
						this.yT = MouseEvent.getY();

					}else {//sinon on d�selectionne
						this.selectedShape.getStrokeDashArray().add(0d);//on remet les bords de la forme en trait pleins
						this.selectedShape = null;//et on r�initialise toutes les variables
						this.isMouseClicked = false;
						this.index = 0;
						this.xT = 0;
						this.yT = 0;
					}
				}
				
			}
		});
		
		drawPane.setOnMouseDragged(MouseEvent ->{
			switch(currentButton.getText()){//on d�termine sur quel bouton on est quand on drag la souris
			case "Line" : {//si c'est le bouton ligne, on r�cup�re le bout de la ligne
				myModel.setXEnd(MouseEvent.getX());
				myModel.setYEnd(MouseEvent.getY());
				break;
			}
			case "Select/Move" :{
				if(isMouseClicked) {//si cest le bouton select, et qu'une forme est s�l�ctionn�e , on translate cette forme avec la souris,� partir du point o� l'on a cliqu� sur la forme
					this.selectedShape.setTranslateX(MouseEvent.getX()-this.xT);
					this.selectedShape.setTranslateY(MouseEvent.getY()-this.yT);
				}
				
				
				
				
			}
			case"Ellipse" : {//pour l'ellipse et le rectangle, on fait la m�me chose que la ligne
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
			if(currentButton != SelectRadio) {//quand on relache le bouton de la souris, et que l'on est en train de dessiner une nouvelle forme, on valide cette nouvelle forme et elle apprait � l'�cran
				this.newShape(Color.BLACK, myModel.getColor());
				this.draw();
				myModel.setXEnd(0);//on r�initialise toutes les variables du mod�le
				myModel.setYEnd(0);
				myModel.setXStart(0);
				myModel.setYStart(0);
			}

		});
		
		
		colorPicker.setOnAction(event ->{//en fonction de l'action sur le color picker, on change la valeur dans le mod�le
			myModel.setColor(colorPicker.getValue());
			if(isMouseClicked) {//si une forme est s�l�ctionn�e, on met � jour la couleur de cette derni�re
				this.updateSelectedShape(colorPicker.getValue());
			}
			
			
		});
		
		
		
		deleteButton.setOnAction(event ->{
			if(this.currentButton == SelectRadio) {//on v�rifie que le bouton s�l�ctionn� est celui de s�l�ction
				Model.getShape().remove(this.selectedShape);//on enl�ve de la liste des shapes � dessiner la shape s�l�ctionn�e
				this.isMouseClicked = false;//on remet tous les attributs � leurs �tats initiaux
				this.index = 0;
				this.xT = 0;
				this.yT = 0;
				this.draw();//on dessine la zone graphique pour acter la suppression de la shape
			}
		});
		

		//Cet algorithme est mal fait selon moi car j'ai essay� de contourner le probl�me du diamant (je ne pouvais pas faire h�riter une classe de Ellipse, Rectangle et line)
		//une meilleur solution aurait surement �t� possible, mais le temps pour l'impl�menter me manque (il aurait surement fallu changer la structure de donn�e de Model)
		cloneButton.setOnAction(event ->{
			try {
				String className = this.selectedShape.getClass().toString();//on r�cup�re le nom de la classe de la shape s�l�ctionn�e
				String arrayShape = Model.getShape().toString();//on r�cup�re l'array de shape de model sous forme de String[] pour s'affranchir du probl�me de typage
				String[] splitted = arrayShape.split(",");//on fait des s�parations au niveau des virgules 
				if(className.contains("Ellipse")) {//on d�tecte la classe ellipse
					Double x = Double.parseDouble(splitted[0].replaceAll("[^0-9]", ""))/10;//on r�cupere la valeur du x de l'ellipse qui nous interr�sse
					Double y = Double.parseDouble(splitted[1].replaceAll("[^0-9]", ""))/10;//celle de y
					Double radiusX = Double.parseDouble(splitted[2].replaceAll("[^0-9]", ""))/10;//on remplace la string par un double, et on divise le tout par 10, le parseur d�calant la virgule, on obtient ainsi le rayon en x de l'ellipse
					Double radiusY = Double.parseDouble(splitted[3].replaceAll("[^0-9]", ""))/10;//et celui en y
					
					Ellipse ell = new Ellipse();//on cr�e une nouvelle ellipse en la d�calant de 10 dans les deux directions
					ell.setCenterX(x+10);
					ell.setCenterY(y+10);
					ell.setRadiusX(radiusX);
					ell.setRadiusY(radiusY);
					ell.setFill(Color.WHITE);
					Model.addShape(ell);//on ajout l'ellipse aux shapes � dessiner
					this.draw();
				}
				if(className.contains("Rectangle")) {
					Double x = Double.parseDouble(splitted[0].replaceAll("[^0-9]", ""))/10;//on pro�de de la m�me mani�re que pour l'ellipse
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
					Double x = Double.parseDouble(splitted[0].replaceAll("[^0-9]", ""))/10;//on se contente de r�cuperer les valeurs de d�but et de fin des lignes
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
	
	public RadioButton getCurrentButton() {//un getter pour le bouton utlis�
		return(this.currentButton);
	}
	
	public void newShape(Color strokeColor, Color fillColor) {//une m�thode pour dessiner une forme en fonction du bouton s�l�ctionn� et de deux couleurs de remplissage et de contour

		switch(currentButton.getText()){
			case "Ellipse" :{
				Ellipse ell = new Ellipse();
				
				ell.setCenterX(myModel.getXStart());
				ell.setCenterY(myModel.getYStart());
				ell.setRadiusX(Math.min(Math.abs(myModel.getXEnd()-myModel.getXStart()),myModel.getXStart()));//on calcul les deux rayons en fonctions du d�calage de la souris par rapport au d�but du mouvement de drag
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
				if(deltaX<0){//on s�pare les cas de la direction de d�placement de la souris et on calcule les x,y,Height et width en fonction du sens de d�placement de la souris
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
		drawPane.getChildren().clear();//on vide tout les enfants de la zone de dessin pour �viter les dupliqu�s
		drawPane.getChildren().addAll(Model.getShape());//on ajoute toutes les shapes de model � la zone de dessin
		
	}
	
	public void updateSelectedShape(Color color) {//une m�thode pour mettre � jour la couleur de la s�l�ction
		this.selectedShape.setFill(color);
	}
}
