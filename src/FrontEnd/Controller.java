package FrontEnd;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import BackEnd.Algorithms;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.AnchorPane;

import javax.sound.sampled.*;

public class Controller implements Initializable {


    @FXML
    private AnchorPane gamePane;

    @FXML
    private AnchorPane homePane;
    @FXML
    private ImageView closeWindow;
    @FXML
    private AnchorPane gameBox;
    @FXML
    private ImageView backButton;
    @FXML
    private Label playerScore;
    @FXML
    private Label agentScore;
    @FXML
    private TextField maxDepth;
    boolean isHome = true;

    private static final int DIAMETER = 56;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;

    private boolean redMove = true;
    private Disc[][] grid = new Disc[COLUMNS][ROWS];

    private Pane discRoot = new Pane();

    //algorithm
    private boolean isPruning;
    private Algorithms algorithms = new Algorithms(7, true);

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        backButton.setVisible(false);
        restart();
    }

    private void restart() {
        redMove = true;
        grid = new Disc[COLUMNS][ROWS];
        gameBox.getChildren().remove(discRoot);
        // initialize the grid
        discRoot = new Pane();
        gameBox.getChildren().add(discRoot);
        gameBox.getChildren().add(makeGrid());
        gameBox.getChildren().addAll(makeColumns());
        agentScore.setText("0");
        playerScore.setText("0");
    }

    private Shape makeGrid() {
        Rectangle rectangle = new Rectangle((COLUMNS + 1) * DIAMETER, (ROWS + 1) * DIAMETER);
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

    private void placeDisc(Disc disc, final int column) {
        int row = ROWS - 1;
        //check if column has no option
        if (grid[column][row] != null)
            return;
        //get the available row
        do {
            if (grid[column][row] != null)
                break;

            row--;
        } while (row >= 0);

        grid[column][row + 1] = disc;
        discRoot.getChildren().add(disc);
        disc.setTranslateX(column * (DIAMETER + 5) + DIAMETER / 4);
        double durationTime = 0.5;
        durationTime = redMove ? 0.5 : 0.7;
        TranslateTransition animation = new TranslateTransition(Duration.seconds(durationTime), disc);
        animation.setToY((ROWS - row - 2) * (DIAMETER + 5) + DIAMETER / 4);
        animation.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // change user scores
                agentScore.setText(Integer.toString(algorithms.agentScore));
                playerScore.setText(Integer.toString(algorithms.humanScore));
            }
        });

        animation.play();
        // change player turn
        redMove = !redMove;
        if (!redMove) {
            playAudio();
            algorithms.updateHuman(column);
            int col = algorithms.minimax();
            placeDisc(new Disc(redMove), col);
        }

    }

    @FXML
    void closeWindowAction(MouseEvent event) {
        Stage stage = (Stage) closeWindow.getScene().getWindow();
        stage.close();
    }

    @FXML
    void triggerScreens(MouseEvent event) {
        if (isHome) {
            restart();
            homePane.setVisible(false);
            gamePane.setVisible(true);
            backButton.setVisible(true);
        } else {
            homePane.setVisible(true);
            gamePane.setVisible(false);
            backButton.setVisible(false);
        }
        isHome = !isHome;

    }

    @FXML
    void selectAlgorithm(MouseEvent event) {
        String buttonType = ((Node) event.getSource()).getId();
        String textState = maxDepth.getText();
        if (inputTextIsValid(textState)) {
            int maxDepthValue = Integer.parseInt(maxDepth.getText());
            if (buttonType.compareTo("minimax") == 0) {
                algorithms = new Algorithms(maxDepthValue, false);
            } else {
                algorithms = new Algorithms(maxDepthValue, true);
            }
            triggerScreens(event);
        }

    }

    @FXML
    void changeButtonColor(MouseEvent event) {
        Node button = ((Node) event.getSource());
        button.setStyle("-fx-background-color: #FFAE78; -fx-background-radius: 20");
    }

    @FXML
    void reChangeButtonColor(MouseEvent event) {
        Node button = ((Node) event.getSource());
        button.setStyle("-fx-background-color:  #2d8da8; -fx-background-radius: 20");
    }

    void playAudio() {
        File file = new File("click.wav");
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private boolean inputTextIsValid(String textState) {
        return textState != null && textState.length() <= 2 && textState.length() >= 1 && isNumeric(textState) && Integer.parseInt(textState) > 0;
    }

    private boolean isNumeric(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
