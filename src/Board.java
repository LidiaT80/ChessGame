import javax.swing.*;
import java.awt.*;

public class Board extends JPanel{

    JPanel[][] fields=new JPanel[8][8];
    ImageIcon[][] pieceImages = new ImageIcon[8][8];
    JLabel[][] fieldLabels =new JLabel[8][8];

    public Board(){
        setLayout(new GridLayout(8,8,3,3));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        setBackground(Color.BLACK);
        setSize(500,500);
    }

    public void createPieces(){
        pieceImages[0][0]=pieceImages[0][7]=new ImageIcon("img/black_rook.png");
        pieceImages[0][1]=pieceImages[0][6]=new ImageIcon("img/black_knight.png");
        pieceImages[0][2]=pieceImages[0][5] =new ImageIcon("img/black_bishop.png");
        pieceImages[0][3]=new ImageIcon("img/black_queen.png");
        pieceImages[0][4]=new ImageIcon("img/black_king.png");
        for (int i = 0; i <8 ; i++) {
            pieceImages[1][i]=new ImageIcon("img/black_pawn.png");
            pieceImages[6][i]=new ImageIcon("img/white_pawn.png");
        }
        pieceImages[7][0]=pieceImages[7][7]=new ImageIcon("img/white_rook.png");
        pieceImages[7][1]=pieceImages[7][6]=new ImageIcon("img/white_knight.png");
        pieceImages[7][2]=pieceImages[7][5] =new ImageIcon("img/white_bishop.png");
        pieceImages[7][3]=new ImageIcon("img/white_queen.png");
        pieceImages[7][4]=new ImageIcon("img/white_king.png");

    }

    public void createBoard(){
        createPieces();
        for (int i = 0; i <8 ; i++) {
            for (int j = 0; j <8 ; j++) {
                fields[i][j]=new JPanel();
                fields[i][j].setLayout(new FlowLayout());
                if((i+j)%2==0)
                    fields[i][j].setBackground(Color.WHITE);
                else
                    fields[i][j].setBackground(Color.BLACK);
                fieldLabels[i][j]=new JLabel(pieceImages[i][j]);

                fields[i][j].add(fieldLabels[i][j]);
                fields[i][j].revalidate();
                fields[i][j].repaint();
                add(fields[i][j]);

            }

        }
    }

    public void move(){                                //Här har jag bara testat att flytta på en pjäs
        fields[6][4].remove(fieldLabels[6][4]);
        fields[5][4].remove(fieldLabels[5][4]);
        fields[5][4].add(fieldLabels[6][4]);
        fields[6][4].revalidate();
        fields[5][4].revalidate();
        fields[6][4].repaint();
        fields[5][4].repaint();
    }




}
