import javax.swing.*;
import java.io.IOException;

public class GameWindow {
    public static void main(String[] args) throws IOException, InterruptedException {
        JFrame frame=new JFrame();
        frame.setSize(600,600);
        Board board=new Board();
        board.createBoard();
        frame.add(board);
        frame.setVisible(true);
        Thread.sleep(3500);
        board.move();

    }

}