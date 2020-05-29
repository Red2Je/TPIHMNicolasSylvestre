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

	private Model myModel;//le modèle lié au controller
	private RadioButton currentButton;//le bouton séléctionné par défaut

	
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
	
	private boolean isMouseClicked = false;// un indicateur de si un élément à été séléctionner
	private Shape selectedShape;//la forme séléctionnée
	private double xT;//l'endroit du clic où l'on a séléctionné la forme
	private double yT;
	private int index;//l'index de la forme séléctionnée dans l'ArrayList de model : shapelist
	public Controller() {
		this.currentButton = SelectRadio;//le bouton par défaut est celui de séléction
		this.myModel = new Model();
		
	}

	public void initialize() {
		
		//On s'occupe des événements sur les boutons : ils s'agit juste de hanger le bouton actif dans le modèle et dans le controlleur
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
		
		//si on appuie sur la souris dans la zone de dessin, avec une séléction autre que le bouton de séléction, on définit le début d'une nouvelle forme
		drawPane.setOnMousePressed(MouseEvent ->{
			if(currentButton != SelectRadio) {
				myModel.setXStart(MouseEvent.getX());
				myModel.setYStart(MouseEvent.getY());}

			
		});
		//si le bouton a été enfoncé puis désenfoncer, on détermine si l'on doit sélectionner une forme ou dessiner une nouvelle shape, et on s'éxécute
		drawPane.setOnMouseClicked(MouseEvent->{
			if(currentButton == SelectRadio) {//cas ou la selection du bouton est "Select/Move"
				Point2D currentPoint = new Point2D(MouseEvent.getX(),MouseEvent.getY());//on récupère les coordonnées de la souris, que l'on transforme en point (pas nécessaire, mais j'avais pas vu le polymorphisme de contains)
				Shape selected = null;//si rien n'est séléctionné, selectedShape vaudra Null
				for(Shape s : Model.getShape()) {//on parcours la liste des shapes et on détermine si une contient notre séléction
					if(s.contains(currentPoint)) {
						selected = s;
					}
				}
				if(selected != null) {//si on en trouve une : 
					if(!isMouseClicked) {//si on n'a pas encore de séléction:
						this.selectedShape = selected;
						this.isMouseClicked = true;
						this.index = Model.getShape().indexOf(selected);//on retrouve l'index de la shape que l'on a séléctionnée
						selected.getStrokeDashArray().add(2d);//on met ses bords en pointillés
						this.xT = MouseEvent.getX();//on définit là où l'on était quand on a cliqué dessus
						this.yT = MouseEvent.getY();

					}else {//sinon on déselectionne
						this.selectedShape.getStrokeDashArray().add(0d);//on remet les bords de la forme en trait pleins
						this.selectedShape = null;//et on réinitialise toutes les variables
						this.isMouseClicked = false;
						this.index = 0;
						this.xT = 0;
						this.yT = 0;
					}
				}
				
			}
		});
		
		drawPane.setOnMouseDragged(MouseEvent ->{
			switch(currentButton.getText()){//on détermine sur quel bouton on est quand on drag la souris
			case "Line" : {//si c'est le bouton ligne, on récupère le bout de la ligne
				myModel.setXEnd(MouseEvent.getX());
				myModel.setYEnd(MouseEvent.getY());
				break;
			}
			case "Select/Move" :{
				if(isMouseClicked) {//si cest le bouton select, et qu'une forme est séléctionnée , on translate cette forme avec la souris,à partir du point où l'on a cliqué sur la forme
					this.selectedShape.setTranslateX(MouseEvent.getX()-this.xT);
					this.selectedShape.setTranslateY(MouseEvent.getY()-this.yT);
				}
				
				
				
				
			}
			case"Ellipse" : {//pour l'ellipse et le rectangle, on fait la même chose que la ligne
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
			if(currentButton != SelectRadio) {//quand on relache le bouton de la souris, et que l'on est en train de dessiner une nouvelle forme, on valide cette nouvelle forme et elle apprait à l'écran
				this.newShape(Color.BLACK, myModel.getColor());
				this.draw();
				myModel.setXEnd(0);//on réinitialise toutes les variables du modèle
				myModel.setYEnd(0);
				myModel.setXStart(0);
				myModel.setYStart(0);
			}

		});
		
		
		colorPicker.setOnAction(event ->{//en fonction de l'action sur le color picker, on change la valeur dans le modèle
			myModel.setColor(colorPicker.getValue());
			if(isMouseClicked) {//si une forme est séléctionnée, on met à jour la couleur de cette dernière
				this.updateSelectedShape(colorPicker.getValue());
			}
			
			
		});
		
		
		
		deleteButton.setOnAction(event ->{
			if(this.currentButton == SelectRadio) {//on vérifie que le bouton séléctionné est celui de séléction
				Model.getShape().remove(this.selectedShape);//on enlève de la liste des shapes à dessiner la shape séléctionnée
				this.isMouseClicked = false;//on remet tous les attributs à leurs états initiaux
				this.index = 0;
				this.xT = 0;
				this.yT = 0;
				this.draw();//on dessine la zone graphique pour acter la suppression de la shape
			}
		});
		

		//Cet algorithme est mal fait selon moi car j'ai essayé de contourner le problème du diamant (je ne pouvais pas faire hériter une classe de Ellipse, Rectangle et line)
		//une meilleur solution aurait surement été possible, mais le temps pour l'implémenter me manque (il aurait surement fallu changer la structure de donnée de Model)
		cloneButton.setOnAction(event ->{
			try {
				String className = this.selectedShape.getClass().toString();//on récupère le nom de la classe de la shape séléctionnée
				String arrayShape = Model.getShape().toString();//on récupère l'array de shape de model sous forme de String[] pour s'affranchir du problème de typage
				String[] splitted = arrayShape.split(",");//on fait des séparations au niveau des virgules 
				if(className.contains("Ellipse")) {//on détecte la classe ellipse
					Double x = Double.parseDouble(splitted[0].replaceAll("[^0-9]", ""))/10;//on récupere la valeur du x de l'ellipse qui nous interrèsse
					Double y = Double.parseDouble(splitted[1].replaceAll("[^0-9]", ""))/10;//celle de y
					Double radiusX = Double.parseDouble(splitted[2].replaceAll("[^0-9]", ""))/10;//on remplace la string par un double, et on divise le tout par 10, le parseur décalant la virgule, on obtient ainsi le rayon en x de l'ellipse
					Double radiusY = Double.parseDouble(splitted[3].replaceAll("[^0-9]", ""))/10;//et celui en y
					
					Ellipse ell = new Ellipse();//on crée une nouvelle ellipse en la décalant de 10 dans les deux directions
					ell.setCenterX(x+10);
					ell.setCenterY(y+10);
					ell.setRadiusX(radiusX);
					ell.setRadiusY(radiusY);
					ell.setFill(Color.WHITE);
					Model.addShape(ell);//on ajout l'ellipse aux shapes à dessiner
					this.draw();
				}
				if(className.contains("Rectangle")) {
					Double x = Double.parseDouble(splitted[0].replaceAll("[^0-9]", ""))/10;//on proède de la même manière que pour l'ellipse
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
					Double x = Double.parseDouble(splitted[0].replaceAll("[^0-9]", ""))/10;//on se contente de récuperer les valeurs de début et de fin des lignes
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
	
	public RadioButton getCurrentButton() {//un getter pour le bouton utlisé
		return(this.currentButton);
	}
	
	public void newShape(Color strokeColor, Color fillColor) {//une méthode pour dessiner une forme en fonction du bouton séléctionné et de deux couleurs de remplissage et de contour

		switch(currentButton.getText()){
			case "Ellipse" :{
				Ellipse ell = new Ellipse();
				
				ell.setCenterX(myModel.getXStart());
				ell.setCenterY(myModel.getYStart());
				ell.setRadiusX(Math.min(Math.abs(myModel.getXEnd()-myModel.getXStart()),myModel.getXStart()));//on calcul les deux rayons en fonctions du décalage de la souris par rapport au début du mouvement de drag
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
				if(deltaX<0){//on sépare les cas de la direction de déplacement de la souris et on calcule les x,y,Height et width en fonction du sens de déplacement de la souris
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
		drawPane.getChildren().clear();//on vide tout les enfants de la zone de dessin pour éviter les dupliqués
		drawPane.getChildren().addAll(Model.getShape());//on ajoute toutes les shapes de model à la zone de dessin
		
	}
	
	public void updateSelectedShape(Color color) {//une méthode pour mettre à jour la couleur de la séléction
		this.selectedShape.setFill(color);
	}
}
