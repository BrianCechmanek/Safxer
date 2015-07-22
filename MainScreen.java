package saver;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;

public class MainScreen extends Application {
	public Circle circle;
	public static Rectangle rect;
	public static Pane canvas;
	private ArrayList<Circle> circleList;
	
	public static void main(String[] args) {
		launch(args);
		System.out.println("Launching fx saver...");
	}
	public void stop() {
		System.out.println("Overridden stop()...");
	}
	public void init() {
		System.out.println("Overridden init()...");
	}
	public void start(Stage mainStage) throws Exception{
		mainStage.setTitle("Sa(fx)er");
		
		//create rootNode, call it canvas by convention
		canvas = new Pane();
		
		//create the scene
		final Scene mainScene = new Scene(canvas, 600, 400);
		
		//create the 'breaker' rectangle in the screen
		rect = new Rectangle(25, 25, Color.BLUE);
		rect.setX(canvas.getBoundsInLocal().getMaxX()/2 - rect.getWidth());
		rect.setY(canvas.getBoundsInLocal().getMaxY()/2 - rect.getHeight());
		canvas.getChildren().add(rect);

		//now we make our movable circle to start
		// and add it to our ArrayList
		circle = new Circle(20, Color.GREEN);
		circle.relocate(75,10);
		circleList = new ArrayList<Circle>();
		circleList.add(circle);
		canvas.getChildren().add(circle);
		
        move(circle);

		//set scene on stage
		mainStage.setScene(mainScene);
		//show the stage
		mainStage.show();
		
		// remove clicked circles
		mainScene.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("MouseClick detected");
				System.out.println("MouseX is: " + event.getSceneX() + "MouseY is: " + event.getSceneY());
				System.out.println("CircleX is: " + circle.getLayoutX() + "CircleY is: " + circle.getLayoutY());
				System.out.println("Circle centreX: " + circle.getCenterX());
				if(circleList.size() >= 2) {
					// if the click is in the circle radius
					if (  (circle.getLayoutX()-circle.getRadius()) <<= event.getSceneX() <<= (circle.getLayoutX()+circle.getRadius())  ) {
						if (  (circle.getLayoutY()-circle.getRadius()) <<= event.getSceneY() <<= (circle.getLayoutY()+circle.getRadius())  ) {
							canvas.getChildren().remove(circleList.get(circleList.size()-1));
							circleList.remove(circleList.size() -1);
							System.out.println("circleList size is now: " + circleList.size());
						}
					}
		                }		
		}});
	}

	public void move(Circle shape) {
		final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
			double deltaX = 2;
			double deltaY = 2;	

		    @Override
            public void handle(final ActionEvent t) {
                final Bounds bounds = canvas.getBoundsInLocal();
                final boolean atRightBorder = shape.getLayoutX() + shape.getRadius() >= (bounds.getMaxX());
                final boolean atLeftBorder = shape.getLayoutX() - shape.getRadius()  <= (bounds.getMinX());
                final boolean atBottomBorder = shape.getLayoutY() >= (bounds.getMaxY() - shape.getRadius());
                final boolean atTopBorder = shape.getLayoutY() <= (bounds.getMinY() + shape.getRadius());

                if (atRightBorder) {
                	deltaX *= -1;
                	shape.setLayoutX(shape.getLayoutX() - shape.getRadius());
                }
                if (atLeftBorder) {
                	deltaX *= -1;
                	shape.setLayoutX(shape.getLayoutX() + shape.getRadius());
                }
                if (atTopBorder) {
                	deltaY *= -1;
                	shape.setLayoutY(shape.getLayoutY() + shape.getRadius());
                }
                if (atBottomBorder) {
                	deltaY *= -1;
                	shape.setLayoutY(shape.getLayoutY() - shape.getRadius());
                }

                shape.setLayoutX(shape.getLayoutX() + deltaX);
                shape.setLayoutY(shape.getLayoutY() + deltaY);
                
                detectCollision(shape);
            }
		    
			public void detectCollision(Circle shape) {
                final boolean inRect = shape.getBoundsInParent().intersects(rect.getBoundsInParent());
				//Should detect if shapes collide and reverse directions (no physics)
				//if the shape hits the center square, it splits into 2
				if (shape.getBoundsInParent().intersects(rect.getBoundsInParent())) {
					deltaX *= -1;
					deltaY *= -1;
	                if (shape != circle) {
	                	if (inRect) {
	                	shape.setLayoutX(shape.getLayoutX() + (5*deltaX));
	                	shape.setLayoutY(shape.getLayoutY() + (5*deltaY));
	                	}
	                }
					if (shape == circle ) {
						if(circleList.size() <= 19) {
					        split(shape);
						    circle.setRadius(circle.getRadius()-1);
					    }
					}
				}
			}
		    }));
		
		    loop.setCycleCount(Timeline.INDEFINITE);
		    loop.play();
	}
	
	public void split(Circle circ) {
		Circle newCircle = new Circle( 7, Color.PURPLE);
		newCircle.relocate(circle.getLayoutX(), circle.getLayoutY());
		circleList.add(newCircle);
		move(newCircle);
		canvas.getChildren().add(newCircle);
	}
}
