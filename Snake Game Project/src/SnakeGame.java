import javax.swing.*;

public class SnakeGame extends JFrame {
    Board board;
    SnakeGame(){
        board = new Board();
        add(board);
        //Packs the parent component to its children component
        //Board dimensions will adapt
        pack();
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        //Initializing the Snake game and v=creating object
        SnakeGame snakeGame = new SnakeGame();
    }
}