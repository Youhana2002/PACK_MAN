import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {



    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int StartX;
        int StartY;
        char direction = 'U';
        int velocityX = 0;
        int velocityY = 0;

        Block(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
            this.StartX = x;
            this.StartY = y;
        }
        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : Walls){
                if (collision(this,wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }
        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -titleSize/4;
            }
            else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = titleSize/4;
            }
            else if (this.direction == 'L') {
                this.velocityX = -titleSize/4;
                this.velocityY = 0;
            }
            else if (this.direction == 'R') {
                this.velocityX = titleSize/4;
                this.velocityY = 0;
            }
        }
        void rest(){
            this.x = this.StartX;
            this.y = this.StartY;
        }
    }


    private int rowCount = 21;
    private int colCount = 19;
    private int titleSize = 32;
    private int boredWidth = colCount * titleSize;
    private int boredHeight = rowCount * titleSize;

    private Image WallImage;
    private Image BlueGhostImage;
    private Image OrangeGhostImage;
    private Image PinkGhostImage;
    private Image RedGhostImage;

    private Image PacManUPImage;
    private Image PacManDownImage;
    private Image PacManLeftImage;
    private Image PacManRightImage;

    // X = wall, O = skip, P = pac man, ' ' = food
    // Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> Walls;
    HashSet<Block> Foods;
    HashSet<Block> Ghosts;
    Block PacMan;

    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    PacMan() {
        setPreferredSize(new Dimension(boredWidth, boredHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);


        WallImage = new ImageIcon(getClass().getResource("/wall.png")).getImage();
        BlueGhostImage = new ImageIcon(getClass().getResource("/blueGhost.png")).getImage();
        RedGhostImage = new ImageIcon(getClass().getResource("/redGhost.png")).getImage();
        PinkGhostImage = new ImageIcon(getClass().getResource("/pinkGhost.png")).getImage();
        OrangeGhostImage = new ImageIcon(getClass().getResource("/orangeGhost.png")).getImage();
        PacManUPImage = new ImageIcon(getClass().getResource("/pacmanUp.png")).getImage();
        PacManDownImage = new ImageIcon(getClass().getResource("/pacmanDown.png")).getImage();
        PacManLeftImage = new ImageIcon(getClass().getResource("/pacmanLeft.png")).getImage();
        PacManRightImage = new ImageIcon(getClass().getResource("/pacmanRight.png")).getImage();

        LoadMap();
        for (Block ghost : Ghosts) {
            char newdirection = directions[random.nextInt(4)];
            ghost.updateDirection(newdirection);
        }
        gameLoop = new Timer(50, this);
        gameLoop.start();

    }

    public void LoadMap() {
        Walls = new HashSet<>();
        Foods = new HashSet<>();
        Ghosts = new HashSet<>();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                String row = tileMap[i];
                char TitleMapChar = row.charAt(j);
                int x = j * titleSize;
                int y = i * titleSize;

                if (TitleMapChar == 'X') { // wall block
                    Block Wall = new Block(x, y, titleSize, titleSize, WallImage);
                    Walls.add(Wall);
                } else if (TitleMapChar == 'b') { // blue ghost
                    Block ghost = new Block(x, y, titleSize, titleSize, BlueGhostImage);
                    Ghosts.add(ghost);
                } else if (TitleMapChar == 'r') {
                    Block ghost = new Block(x, y, titleSize, titleSize, RedGhostImage);
                    Ghosts.add(ghost);
                } else if (TitleMapChar == 'p') {
                    Block ghost = new Block(x, y, titleSize, titleSize, PinkGhostImage);
                    Ghosts.add(ghost);
                } else if (TitleMapChar == 'o') {
                    Block ghost = new Block(x, y, titleSize, titleSize, OrangeGhostImage);
                    Ghosts.add(ghost);
                } else if (TitleMapChar == 'P') {
                    PacMan = new Block(x, y, titleSize, titleSize, PacManRightImage);
                } else if (TitleMapChar == ' ') { // food
                    Block food = new Block(x + 14, y + 14, 4, 4, null);
                    Foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g) {
        g.drawImage(PacMan.image,PacMan.x, PacMan.y, PacMan.width, PacMan.height,null);
        for (Block ghost : Ghosts) {
            g.drawImage(ghost.image,ghost.x, ghost.y, ghost.width, ghost.height,null);
        }
        for (Block Wall : Walls) {
            g.drawImage(Wall.image,Wall.x, Wall.y, Wall.width, Wall.height,null);
        }
        g.setColor(Color.WHITE);
        for (Block Food : Foods) {
            g.fillRect(Food.x, Food.y, Food.width, Food.height);
        }
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        if (gameOver) {
            g.drawString("GAME OVER : "+ String.valueOf(score), titleSize/2, titleSize/2);
        }
        else {
            g.drawString("x"+ String.valueOf(lives) + " Score : "+String.valueOf(score), titleSize/2, titleSize/2);
        }
    }
    public void move() {
        PacMan.x += PacMan.velocityX;
        PacMan.y += PacMan.velocityY;

        for (Block wall : Walls) {
            if (collision(PacMan, wall)) {
                PacMan.x -= PacMan.velocityX;
                PacMan.y -= PacMan.velocityY;
                break;
            }
        }

        for (Block ghost : Ghosts) {
            if (collision(PacMan, ghost)) {
                lives -= 1 ;
                if(lives == 0) {
                    gameOver = true;
                    return;
                }
                restPositions();
            }
            if (ghost.y == titleSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');
            }

            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            boolean hit = false;

            if (ghost.x < 0 || ghost.y < 0 ||
                    ghost.x + ghost.width > boredWidth ||
                    ghost.y + ghost.height > boredHeight) {
                hit = true;
            } else {
                // تحقق من الاصطدام مع الحيطان
                for (Block wall : Walls) {
                    if (collision(ghost, wall)) {
                        hit = true;
                        break;
                    }
                }
            }

            if (hit) {
                ghost.x -= ghost.velocityX;
                ghost.y -= ghost.velocityY;
                char newdirection = directions[random.nextInt(4)];
                ghost.updateDirection(newdirection);
            }
        }
        Block foodEaten = null;
        for (Block food : Foods) {
            if ( collision(PacMan, food) ) {
                foodEaten = food;
                score +=10 ;
            }
        }
        Foods.remove(foodEaten);
        if (Foods.isEmpty()) {
            LoadMap();
            restPositions();
        }
    }

    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width && a.x + a.width > b.x &&
                a.y < b.y + b.height && a.y + a.height > b.y;
    }

    public void restPositions() {
        PacMan.rest();
        PacMan.velocityX = 0;
        PacMan.velocityY = 0;
        for (Block ghost : Ghosts) {
            ghost.rest();
            char newdirection = directions[random.nextInt(4)];
            ghost.updateDirection(newdirection);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
            if (gameOver) {
                gameLoop.stop();
            }
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
//        System.out.println("keyReleased: " + e.getKeyCode());
        if (gameOver) {
            LoadMap();
            restPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            PacMan.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            PacMan.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            PacMan.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            PacMan.updateDirection('R');
        }
        if(PacMan.direction == 'U'){
            PacMan.image = PacManUPImage;
        }
        else if(PacMan.direction == 'D'){
            PacMan.image = PacManDownImage ;
        }
        else if(PacMan.direction == 'L'){
            PacMan.image = PacManLeftImage;
        }
        else if(PacMan.direction == 'R'){
            PacMan.image = PacManRightImage;
        }
    }
}
