package com.oop.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.oop.Main;
import com.oop.model.Diamond;
import com.oop.model.Gold;
import com.oop.model.Mole;
import com.oop.model.Rock;

public class MainGame extends Application {

    private static final int NUM_LEVELS = 6;
    private static final int[] TARGET_SCORES = {100, 200, 300, 400, 500, 600}; // Example target scores for each level
    private int currentLevel = 0;
    private int currentScore = 0;
    private int timeRemaining = 60;
    private Timeline gameTimeline;
    private Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label targetScoreLabel;
    private Pane root;
    private boolean isSoundPlaying = false;

    @SuppressWarnings("exports")
    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, 657, 480);
        root.setStyle("-fx-background-image: url('file:src/main/resources/image/background/mainGame_background.png'); -fx-background-size: cover;");

        scoreLabel = new Label("Score: 0");
        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(10);
        scoreLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 24px; -fx-text-fill: white;");

        timeLabel = new Label("Time: 60");
        timeLabel.setLayoutX(10);
        timeLabel.setLayoutY(50);
        timeLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 24px; -fx-text-fill: white;");

        levelLabel = new Label("Level: 1");
        levelLabel.setLayoutX(500);
        levelLabel.setLayoutY(10);
        levelLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 24px; -fx-text-fill: white;");

        targetScoreLabel = new Label("Target: 100");
        targetScoreLabel.setLayoutX(500);
        targetScoreLabel.setLayoutY(50);
        targetScoreLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 24px; -fx-text-fill: white;");

        root.getChildren().addAll(scoreLabel, timeLabel, levelLabel, targetScoreLabel);

        primaryStage.setTitle("Gem_miner_OOP20232");
        primaryStage.setScene(scene);
        primaryStage.show();

        startLevel();
    }

    private void startLevel() {
        timeRemaining = 10; 
        currentScore = 0; 
        updateScore();
        updateTime();
        updateLevelInfo();
        isSoundPlaying=false;

        if (gameTimeline != null) {
            gameTimeline.stop();
        }

        gameTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateGame()));
        gameTimeline.setCycleCount(Timeline.INDEFINITE);
        gameTimeline.play();

        // Clear previous objects
        root.getChildren().removeIf(node -> node instanceof ImageView);

        for (int i = 0; i < 7; i++) {
            int minRange = 150;
            int maxRange = 450;
            int x = (int) (Math.random() * (maxRange - minRange + 1)) + minRange;
            int y = (int) (Math.random() * (maxRange - minRange + 1)) + minRange;
            System.out.println(x + " " + y);

            Diamond diamond = new Diamond(0, 0, i%2);
            Mole hehe = new Mole(x, y,x-200,x+50,0);
            diamond.caughtFlag=true;
            ImageView imageView = diamond.getImageView(1);
            ImageView imageView2 = hehe.getImageView(1);
            //hehe.move(imageView2, hehe.getSpeed());
            //thu nhỏ imageView giảm 1/2
            imageView2.setScaleX(0.25/Main.scale);
            imageView2.setScaleY(0.25/Main.scale);
            imageView2.setLayoutX(x);
            imageView2.setLayoutY(y);
            imageView.setScaleX(0.25/Main.scale);
            imageView.setScaleY(0.25/Main.scale);
            imageView.setLayoutX(x);
            imageView.setLayoutY(y);
            root.getChildren().add(imageView);
            root.getChildren().add(imageView2);
            // Add click handler to collect gold
            imageView.setOnMouseClicked(event -> {
                PlayMusic thuvatpham = new PlayMusic();
                thuvatpham.SetMusicPath("src/main/resources/music/thuvatpham.wav");
                thuvatpham.run();
                currentScore += 100; // Example score increment
                root.getChildren().remove(imageView);
                updateScore();
            });
            imageView2.setOnMouseClicked(event -> {
                PlayMusic MouseSound = new PlayMusic();
                MouseSound.SetMusicPath("src/main/resources/music/MouseSound.mp3");
                MouseSound.run();
                currentScore += hehe.getVal(); // Example score increment
                root.getChildren().remove(imageView2);
                updateScore();
            });
        }
    }

    private void updateGame() {

        if (timeRemaining >= 1) timeRemaining--;
        if(timeRemaining<=9&& timeRemaining>8){
            if(!isSoundPlaying){
            PlayMusic ingame = new PlayMusic();
            ingame.SetMusicPath("src/main/resources/music/ingame.wav");
            ingame.run();
            }
        }
        updateTime();

        if (timeRemaining <= 0) {
            if (currentScore >= TARGET_SCORES[currentLevel]) {
                if(currentLevel<5){
                 if(!isSoundPlaying){
                    PlayMusic victoryRound = new PlayMusic();
                    victoryRound.SetMusicPath("src/main/resources/music/victoryRound.wav");
                    victoryRound.run();
                    isSoundPlaying = true;
                    }
                }
                currentLevel++;
                if (currentLevel < NUM_LEVELS) {
                    showLevelCompletedMessage();
                } else {
                    // Player has completed all levels
                    PlayMusic victoryFinal = new PlayMusic();
                    victoryFinal.SetMusicPath("src/main/resources/music/victoryFinal.MP3");
                    victoryFinal.run();
                    gameTimeline.stop();
                    victoryFinal.stopMusic();
                    showEndMessage("Congratulations! You've completed all levels!");
                }
            } else {
                // Game over
                PlayMusic soundLose = new PlayMusic();
                soundLose.SetMusicPath("src/main/resources/music/lose1.1.wav");
                soundLose.run();
                gameTimeline.stop();
                showEndMessage("Game Over! You did not reach the target score.");
            }
        }
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + currentScore);
    }

    private void updateTime() {
        timeLabel.setText("Time: " + timeRemaining);
    }

    private void updateLevelInfo() {
        levelLabel.setText("Level: " + (currentLevel + 1));
        targetScoreLabel.setText("Target: " + TARGET_SCORES[currentLevel]);
    }

    private void showLevelCompletedMessage() {
        gameTimeline.stop(); // Stop the game timeline

        // Create a full-screen overlay
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlay.setPrefSize(657, 480);

        VBox messageBox = new VBox();
        messageBox.setStyle("-fx-alignment: center; -fx-spacing: 20px;");
        
        ImageView winPopup = new ImageView(new Image("file:src/main/resources/image/popup/win_pop_up.png"));
        winPopup.setFitWidth(450);
        winPopup.setFitHeight(300);

        Label messageLabel = new Label("Next level will start in 5 seconds");
        messageLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");

        messageBox.getChildren().addAll(winPopup, messageLabel);
        overlay.getChildren().add(messageBox);

        root.getChildren().add(overlay);

        // Wait for 5 seconds before starting the next level
        Timeline waitTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            root.getChildren().remove(overlay);
            startLevel();
        }));
        waitTimeline.setCycleCount(1); // Ensure this only runs once
        waitTimeline.play();
    }

    private void showEndMessage(String message) {
        gameTimeline.stop(); // Stop the game timeline

        // Create a full-screen overlay
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlay.setPrefSize(657, 480);

        VBox messageBox = new VBox();
        messageBox.setStyle("-fx-alignment: center; -fx-spacing: 20px;");

        ImageView losePopup = new ImageView(new Image("file:src/main/resources/image/popup/lose_pop_up.png"));
        losePopup.setFitWidth(375);
        losePopup.setFitHeight(300);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label exitMessageLabel = new Label("Click 'X' to exit the game");
        exitMessageLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        messageBox.getChildren().addAll(losePopup, messageLabel, exitMessageLabel);
        overlay.getChildren().add(messageBox);

        root.getChildren().add(overlay);
}

    public static void main(String[] args) {
        launch(args);
    }
}
