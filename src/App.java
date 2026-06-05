import javax.swing.JFrame;


public class App  {
    public static void main(String[] args) {
        int rowCount = 21;
        int colCount = 19;
        int titleSize = 32;
        int boredWidth = colCount * titleSize;
        int boredHeight = rowCount * titleSize;

        JFrame frame = new JFrame("Pacman");
//        frame.setVisible(true);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boredWidth, boredHeight);

        PacMan PacmanGame = new PacMan();
        frame.add(PacmanGame);
        frame.pack();
        PacmanGame.requestFocus();
        frame.setVisible(true);
    }
}
