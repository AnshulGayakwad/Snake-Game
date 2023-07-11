import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int B_WIDTH = 400;  //Frame Width
    int B_HEIGHT = 400; //Frame Height

    int MAX_DOTS = 1600;//Max Dots in the frame which is B_WIDTH*B_HEIGHT
    int DOT_SIZE = 10;  //Size of single dot
    int DOTS;           //length of the snake
    int x[] = new int[MAX_DOTS];    //Storing coordinates with X-axis
    int y[] = new int[MAX_DOTS];    //Storing coordinates with X-axis

    int apple_x;    //coordinates of apple
    int apple_y;
    Image body, head, apple;
    Timer timer;
    int DELAY = 150;    //Indicate the refreshing time in millisecond
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    boolean isGameOn = true;

    Board(){
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setBackground(Color.BLACK);
        initGame();
        loadImages();
    }

    public void initGame(){ //Initializing game with starting position of snake coordinates
        DOTS = 3;      //Initial size of snake with 3 dots
        //Initialize snake's first position of dot,
        x[0] = 200;
        y[0] = 200;
        for (int i=1; i<DOTS; i++){
            x[i] = x[0] + DOT_SIZE*i;
            y[i] = y[0];
        }
        //locate random position of apple inside board
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    //load images from resources folder to image object
    public void loadImages(){
        //load body image
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        //load head image
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        //load apple image
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }

    //Draw Images of head & dot at snake's position and apple at apple's position
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }

    //draw Images
    public void doDrawing(Graphics g){
        if(isGameOn){
            //draw apple image
            g.drawImage(apple, apple_x, apple_y, this);
            //draw for snake
            for (int i=0; i<DOTS; i++){
                //check for head position
                if(i==0){
                    //place head image
                    g.drawImage(head, x[0], y[0], this);
                }
                else { // place body (dot) image
                    g.drawImage(body, x[i], y[i], this);
                }
            }
        }
        else {
            gameOver(g);
            timer.stop();
        }
    }

    //Locate randomize apple position on board
    public void locateApple(){
        //multiply by 39 because we always want position inside the board size
        //and also we always want position in multiple of DOT_SIZE,
        //then after both snake head and apple position will match
        apple_x = ((int)(Math.random()*39)) * DOT_SIZE;
        apple_y = ((int)(Math.random()*39)) * DOT_SIZE;
    }


    //Check collision with body and border
    public void checkCollision(){
        //collision with body
        for(int i=1; i<DOTS; i++){
            //for collision with body snake length should greater than size 4
            //and head position must me same as collided body part (specific dot)
            if(i>4 && x[0]==x[i] && y[0]==y[i]){
                isGameOn = false;
            }
        }

        //collision with Border
        if(x[0]<0 || x[0]>=B_WIDTH || y[0]<0 || y[0]>=B_HEIGHT){
            isGameOn = false;
        }
    }


    //Display Game Over message
    public void gameOver(Graphics g){
        String msg = "Game Over ☠";
        int score = (DOTS - 3)*100;
        String scoreMsg = "Score : "+Integer.toString(score);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        //this will give width & height of the Font
        FontMetrics fontMetrics = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        //we have to print message at the center of frame for that -> width == |--x--message--x--|
        // message should be at 2nd part of the total height
        g.drawString(msg, (B_WIDTH-fontMetrics.stringWidth(msg))/2, B_HEIGHT/4);

        //we have to print ScoreMessage at the center of frame for that -> width == |--x--message--x--|
        // ScoreMessage should be at 3rd part of the total height
        g.drawString(scoreMsg, (B_WIDTH-fontMetrics.stringWidth(scoreMsg))/2, 3*(B_HEIGHT/4));
    }


    @Override
    public void actionPerformed(ActionEvent activeEvent){
        if(isGameOn){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }
    //make snake move
    public void move(){
        for (int i=DOTS-1; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftDirection){
            x[0] -= DOT_SIZE;
        }
        if(rightDirection){
            x[0] += DOT_SIZE;
        }
        if(upDirection){
            y[0] -= DOT_SIZE;
        }
        if(downDirection){
            y[0] += DOT_SIZE;
        }
    }

    //make snake eat apple
    public void checkApple(){
        //check if head position & apple position is same
        if(apple_x == x[0] && apple_y == y[0]){
            //we must increase the snake size
            DOTS++;
            //we must locate another apple on the board
            locateApple();
        }
    }

    //Implement controls (left, right, up, down)
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();

            //check if left key is pressed, and we must not in opposite direction
            if(key == KeyEvent.VK_LEFT && !rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            //check if right key is pressed
            if(key == KeyEvent.VK_RIGHT && !leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            //check if up key is pressed
            if(key == KeyEvent.VK_UP && !downDirection){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            //check if down key is pressed
            if(key == KeyEvent.VK_DOWN && !upDirection){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
