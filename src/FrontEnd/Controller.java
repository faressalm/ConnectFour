package FrontEnd;
import java.net.URL;
import java.util.ResourceBundle;

import BackEnd.Algorithms;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
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

    boolean isHome =true;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {


    }


    @FXML
    void closeWindowAction(MouseEvent event) {
        Stage stage = (Stage) closeWindow.getScene().getWindow();
        stage.close();
    }


    @FXML
    void triggerScreens(MouseEvent event) {
         if(isHome){
             homePane.setVisible(false);
             gamePane.setVisible(true);
         }else{
             homePane.setVisible(true);
             gamePane.setVisible(false);
         }
         isHome=!isHome;
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