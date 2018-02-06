import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {

    JPanel[][] fields = new JPanel[8][8];
    ImageIcon[][] pieceImages = new ImageIcon[8][8];
    JLabel[][] fieldLabels = new JLabel[8][8];
    ImageIcon blackRook = new ImageIcon("img/black_rook1.png");
    ImageIcon blackKnight = new ImageIcon("img/black_knight1.png");
    ImageIcon blackBishop = new ImageIcon("img/black_bishop1.png");
    ImageIcon blackQueen = new ImageIcon("img/black_queen1.png");
    ImageIcon blackKing = new ImageIcon("img/black_king1.png");
    ImageIcon blackPawn = new ImageIcon("img/black_pawn1.png");
    ImageIcon whitePawn = new ImageIcon("img/white_pawn1.png");
    ImageIcon whiteRook = new ImageIcon("img/white_rook1.png");
    ImageIcon whiteKnight = new ImageIcon("img/white_knight1.png");
    ImageIcon whiteBishop = new ImageIcon("img/white_bishop1.png");
    ImageIcon whiteQueen = new ImageIcon("img/white_queen1.png");
    ImageIcon whiteKing = new ImageIcon("img/white_king1.png");

    public Board() {
        setLayout(new GridLayout(8, 8));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        setBackground(Color.BLACK);
        setSize(500, 500);
    }

    public void createPieces() {
        pieceImages[0][0] = pieceImages[0][7] = blackRook;
        pieceImages[0][1] = pieceImages[0][6] = blackKnight;
        pieceImages[0][2] = pieceImages[0][5] = blackBishop;
        pieceImages[0][3] = blackQueen;
        pieceImages[0][4] = blackKing;
        for (int i = 0; i < 8; i++) {
            pieceImages[1][i] = blackPawn;
            pieceImages[6][i] = whitePawn;
        }
        pieceImages[7][0] = pieceImages[7][7] = whiteRook;
        pieceImages[7][1] = pieceImages[7][6] = whiteKnight;
        pieceImages[7][2] = pieceImages[7][5] = whiteBishop;
        pieceImages[7][3] = whiteQueen;
        pieceImages[7][4] = whiteKing;

    }

    public void createBoard() {
        createPieces();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                fields[i][j] = new JPanel();
                fields[i][j].setLayout(new FlowLayout());
                if ((i + j) % 2 == 0)
                    fields[i][j].setBackground(Color.LIGHT_GRAY);
                else
                    fields[i][j].setBackground(Color.GRAY);
                fieldLabels[i][j] = new JLabel(pieceImages[i][j]);

                fields[i][j].add(fieldLabels[i][j]);
                fields[i][j].revalidate();
                fields[i][j].repaint();
                add(fields[i][j]);

            }

        }
    }

    public void move() {                                //Här har jag bara testat att flytta på en pjäs
        fields[6][4].remove(fieldLabels[6][4]);
        fields[5][4].remove(fieldLabels[5][4]);
        fields[5][4].add(fieldLabels[6][4]);
        fields[6][4].revalidate();
        fields[5][4].revalidate();
        fields[6][4].repaint();
        fields[5][4].repaint();
    }


}
