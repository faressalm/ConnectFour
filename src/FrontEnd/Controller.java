package FrontEnd;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.AnchorPane;

public class Controller implements Initializable {


    @FXML
    private AnchorPane gamePane;

    @FXML
    private AnchorPane homePane;
    @FXML
    private ImageView closeWindow;
    @FXML
    private AnchorPane gameBox;
    boolean isHome =true;

    private static final int DIAMETER = 60;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;

    private boolean redMove = true;
    private Disc[][] grid = new Disc[COLUMNS][ROWS];

    private Pane discRoot = new Pane();
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        restart();
    }

    private Shape makeGrid() {
       Rectangle rectangle= new Rectangle((COLUMNS + 1) * DIAMETER, (ROWS + 1) * DIAMETER);
        rectangle.setArcWidth(30.0);
        rectangle.setArcHeight(30.0);
        Shape shape = rectangle;
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle(DIAMETER / 2);
                circle.setCenterX(DIAMETER / 2);
                circle.setCenterY(DIAMETER / 2);
                circle.setTranslateX(x * (DIAMETER + 5) + DIAMETER / 4);
                circle.setTranslateY(y * (DIAMETER + 5) + DIAMETER / 4);

                shape = Shape.subtract(shape, circle);
            }
        }
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(100);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        shape.setFill(Color.web("#FFAE78"));
        shape.setEffect(lighting);
        return shape;
    }
    private List<Rectangle> makeColumns() {
        List<Rectangle> list = new ArrayList<Rectangle>();

        for (int x = 0; x < COLUMNS; x++) {
            final Rectangle rect = new Rectangle(DIAMETER, (ROWS + 1) * DIAMETER);
            rect.setTranslateX(x * (DIAMETER + 5) + DIAMETER / 4);
            rect.setFill(Color.TRANSPARENT);

            rect.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    rect.setFill(Color.rgb(207, 194, 97, 0.5));
                }
            });
            rect.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    rect.setFill(Color.TRANSPARENT);
                }
            });
            final int column = x;
            rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    Controller.this.placeDisc(new Disc(redMove), column);
                }
            });
            list.add(rect);
        }

        return list;
    }
    private static class Disc extends Circle {
        public Disc(boolean isPlayerOne) {
            super(DIAMETER / 2, isPlayerOne ? Color.rgb(181, 50, 50, 1) : Color.rgb(33, 184, 176, 1.0));
            setCenterX(DIAMETER / 2);
            setCenterY(DIAMETER / 2);
        }
    }
    private void placeDisc(Disc disc, int column) {
        int row = ROWS - 1;
        //check if column has no option
        if(grid[column][row]!=null)
            return;
        //get the available row
        do {
            if (grid[column][row]!=null)
                break;

            row--;
        } while (row >= 0);

        grid[column][row+1] = disc;
        discRoot.getChildren().add(disc);
        disc.setTranslateX(column * (DIAMETER + 5) + DIAMETER / 4);
        // change player turn
        redMove = !redMove;

        final int currentRow = row;

        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), disc);
        animation.setToY((ROWS-row-2) * (DIAMETER + 5) + DIAMETER / 4);
        animation.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
//            if (gameEnded(column, currentRow)) {
//                gameOver();
//            }
            }
        });
        animation.play();
    }
    
    @FXML
    void closeWindowAction(MouseEvent event) {
        Stage stage = (Stage) closeWindow.getScene().getWindow();
        stage.close();
    }
    @FXML
    void triggerScreens(MouseEvent event) {
        if(isHome){
            restart();
            homePane.setVisible(false);
            gamePane.setVisible(true);
        }else{
            homePane.setVisible(true);
            gamePane.setVisible(false);
        }
        isHome=!isHome;

    }

    private void restart(){
        redMove=true;
        grid = new Disc[COLUMNS][ROWS];
        gameBox.getChildren().remove(discRoot);
        // initialize the grid
        discRoot= new Pane();
        gameBox.getChildren().add(discRoot);
        gameBox.getChildren().add(makeGrid());
        gameBox.getChildren().addAll(makeColumns());
    }
}


// translate

//        TranslateTransition translate = new TranslateTransition();
//
//        translate.setNode(myImage);
//
//        translate.setDuration(Duration.millis(1000));
//
//        translate.setCycleCount(TranslateTransition.INDEFINITE);
//
//        translate.setByX(500);
//
//        translate.setByY(-250);
//
//        translate.setAutoReverse(true);
//
//        translate.play();

//        ScaleTransition scale = new ScaleTransition();
//
//        scale.setNode(myImage);
//
//        scale.setDuration(Duration.millis(1000));
//
//        scale.setCycleCount(TranslateTransition.INDEFINITE);
//
//        scale.setInterpolator(Interpolator.LINEAR);
//
//        scale.setByX(2.0);
//
//        scale.setByY(2.0);
//
//        scale.setAutoReverse(true);
//
//        scale.play();
/*

  // rotate

  RotateTransition rotate = new RotateTransition();

  rotate.setNode(myImage);

  rotate.setDuration(Duration.millis(500));

  rotate.setCycleCount(TranslateTransition.INDEFINITE);

  rotate.setInterpolator(Interpolator.LINEAR);

  rotate.setByAngle(360);

  rotate.setAxis(Rotate.Z_AXIS);

  rotate.play();



  // fade

  FadeTransition fade = new FadeTransition();

  fade.setNode(myImage);

  fade.setDuration(Duration.millis(1000));

  fade.setCycleCount(TranslateTransition.INDEFINITE);

  fade.setInterpolator(Interpolator.LINEAR);

  fade.setFromValue(0);

  fade.setToValue(1);

  fade.play();



  // scale

  ScaleTransition scale = new ScaleTransition();

  scale.setNode(myImage);

  scale.setDuration(Duration.millis(1000));

  scale.setCycleCount(TranslateTransition.INDEFINITE);

  scale.setInterpolator(Interpolator.LINEAR);

  scale.setByX(2.0);

  scale.setByY(2.0);

  scale.setAutoReverse(true);

  scale.play();

  */