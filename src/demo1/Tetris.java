package demo1;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
编写俄罗斯方块主类
 */
public class Tetris extends JPanel {

//    正在下落的四方块
    private Tetromino currentOne = Tetromino.randomOne();
//    下一个要下落的四方块
    private Tetromino nextOne = Tetromino.randomOne();
//    游戏区域
    private Cell[][] wall = new Cell[18][9];
//    声明一个单元格的像素(48像素)
    public static final int CELL_SIZE = 48;

//    声明游戏分数相关的变量(分数池,分数统计,消除行数)
//    分数池（消除0行0分，1行1分，2行2分，3行5分，4行10分）
    int[] scores_pool = {0,1,2,5,10};
//    分数统计
    private int totalScore = 0;
//    消除行数
    private int totalLine = 0;

//    声明游戏的三种状态，分别是：游戏中，暂停，游戏结束
    public static final int PLAYING = 0;
    public static final int PAUSE = 1;
    public static final int GAMEOVER = 2;
//    声明一个变量保存当前游戏的状态
    private int game_state;
//    声明一个数组来显示游戏的状态
    String[] show_state = {"P[暂停]","C[继续]","S[重开]"};

//    载入方块图片
    public static BufferedImage I;
    public static BufferedImage J;
    public static BufferedImage L;
    public static BufferedImage O;
    public static BufferedImage S;
    public static BufferedImage T;
    public static BufferedImage Z;
//   载入游戏背景图片
    public static BufferedImage backgroundImage;

    static {
        try {
            I = ImageIO.read(new File("images/I.png"));
            J = ImageIO.read(new File("images/J.png"));
            L = ImageIO.read(new File("images/L.png"));
            O = ImageIO.read(new File("images/O.png"));
            S = ImageIO.read(new File("images/S.png"));
            T = ImageIO.read(new File("images/T.png"));
            Z = ImageIO.read(new File("images/Z.png"));
            backgroundImage = ImageIO.read(new File("images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//  今天太累了，明天写注释
    public void start(){
        game_state = PLAYING;
        KeyListener listener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                switch(code){
                    case KeyEvent.VK_DOWN:
                        softDropAction();
                        break;
                    case KeyEvent.VK_LEFT:
                        moveLeftAction();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRightAction();
                        break;
                    case KeyEvent.VK_UP:
                        rotateRightAction();
                        break;
                    case KeyEvent.VK_SPACE:
                        handDropAction();
                        break;
                    case KeyEvent.VK_0:
                        rotateLeftAction();
                        break;
                    case KeyEvent.VK_P:
                        if(game_state == PLAYING){
                            game_state = PAUSE;
                        }
                        break;
                    case KeyEvent.VK_C:
                        if(game_state == PAUSE){
                            game_state = PLAYING;
                        }
                        break;
                    case KeyEvent.VK_S:
                        game_state = PLAYING;
                        wall = new Cell[18][9];
                        currentOne = Tetromino.randomOne();
                        nextOne = Tetromino.randomOne();
                        totalScore = 0;
                        totalLine = 0;
                        break;
                }
            }
        };

//        将程序设置为焦点
        this.addKeyListener(listener);
        this.requestFocus();

        while(true){
            if(game_state == PLAYING){
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(canDrop()){
                    currentOne.softDrop();
                }else{
                    landToWall();
                    destroyLine();
                    if(isGameOver()){
                        game_state = GAMEOVER;
                    }else{
                        currentOne = nextOne;
                        nextOne = Tetromino.randomOne();
                    }
                }
            }
            repaint();
        }
    }

//    编写顺时针旋转
    public void rotateRightAction(){
//        当前四方块顺时针旋转
        currentOne.retateRight();
//        如果越界或者重合，回到原来的位置
        if(outOfBounds() || coincide()){
            currentOne.retateLeft();
        }
    }

//    编写逆时针旋转
    public void rotateLeftAction(){
        currentOne.retateLeft();
        if(outOfBounds() || coincide()){
            currentOne.retateRight();
        }
    }

    /**
     * 瞬间下落
     */
    public void handDropAction(){

        while(true){
            if(canDrop()){
                currentOne.softDrop();
            }else{
                break;
            }
        }
//      如果不能下落，就嵌入到墙中
        landToWall();
//     然后判断能否消行
        destroyLine();
//     然后判断游戏是否结束
        if(isGameOver()){
//     如果游戏结束就更改状态
            game_state = GAMEOVER;
        }else{
//     如果游戏没结束，下一个四方块变成当前的四方块，下一个四方块再生成一个新的四方块
            currentOne = nextOne;
            nextOne = Tetromino.randomOne();
        }
    }

    /**
     * 四方格按键下落方法
     */
    public void softDropAction() {
//        判断是否能下落
        if(canDrop()){
//            如果可以下落，当前四方格下落一格
            currentOne.softDrop();
        }else{
//            如果不能下落，就嵌入到墙中
            landToWall();
//            然后判断能否消行
            destroyLine();
//            然后判断游戏是否结束
            if(isGameOver()){
//                如果游戏结束就更改状态
                game_state = GAMEOVER;
            }else{
//                如果游戏没结束，下一个四方块变成当前的四方块，下一个四方块再生成一个新的四方块
                currentOne = nextOne;
                nextOne = Tetromino.randomOne();
            }
        }
    }

    /**
     * 嵌入
     */
    private void landToWall() {
//        获取当前四方块
        Cell[] cells = currentOne.cells;
//        遍历嵌入二维数组
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            wall[row][col] = cell;
        }
    }

    /**
     * 判断能否下落
     * @return 返回true表示可以继续下落，返回false表示不能继续下落
     */
    public boolean canDrop(){
//        获取当前四方格
        Cell[] cells = currentOne.cells;
//        遍历
        for (Cell cell : cells) {
//            获取行列值
            int row = cell.getRow();
            int col = cell.getCol();
//            如果到底部，或者该单元格下面有元素就不能下落
            if(row == wall.length - 1){
                return false;
            }else if(wall[row + 1][col] != null){
                return false;
            }
        }
        return true;
    }

    /**
     * 消除行数
     */
    public void destroyLine() {
//        定义一个变量统计一次消除的行数
        int countLine = 0;
//        获取正在下落的四方块
        Cell[] cells = currentOne.cells;
//        遍历
        for (Cell cell : cells) {
//            获取单元格行数
            int row = cell.getRow();
//            下面判断，如果该单元格所在的行满了，就消掉
            if (isFullLine(row)) {
//                一次性消掉的行数加1
                countLine++;
//                然后把消掉该行，并把该行上方的所有单元格向下移动一格
                for(int i = row; i > 0; i--){
//                    用数组拷贝，把上方的行拷贝下来
                    System.arraycopy(wall[i - 1],0,wall[i],0,wall[0].length);
                }
//                上方拷贝完之后，最上方的一行肯定没有任何东西，所以重新赋值
                wall[0] = new Cell[9];
            }
        }

//        统计本次消除行数的分数
        totalScore += scores_pool[countLine];
//        统计本次消除的行数
        totalLine += countLine;
    }

        /**
         * 判断行是否已满
         * @param row 行数
         * @return 返回true表示该行已满，返回false表示该行未满
         */
    public boolean isFullLine(int row){
//        获取这行上的所有元素，是一个Cell数组
        Cell[] cells = wall[row];
//        遍历，判断里面的元素是否满了
        for (Cell cell : cells) {
//            如果有一个元素为空，则表示没满，返回false
            if(cell == null){
                return false;
            }
        }
        return true;
    }


    /**
     * 判断游戏是否结束
     * @return 返回true表示结束，返回false表示未结束
     */
    public boolean isGameOver(){
//        获取下一个四方块
        Cell[] cells = nextOne.cells;
//        遍历（如果下一个四方块没有地方放置，表示游戏结束）
        for (Cell cell : cells) {
//            获取行列值
            int row = cell.getRow();
            int col = cell.getCol();
            if(wall[row][col] != null){
                return true;
            }
        }
        return false;
    }

//    重写print方法
    @Override
    public void paint(Graphics g) {
//        把背景图片绘制进去，并调整位置(运行后调整)
        g.drawImage(backgroundImage,0,0,null);
//        平移坐标轴(水平移动22像素，上下移动15像素)
        g.translate(22,15);
//        绘制游戏区域(这里基于（0,0）点绘制的区域有偏差，所以在上面平移坐标轴)
        printWall(g);
//        绘制正在下落的四方格
        printCurrentOne(g);
//        绘制下一个要下落的四方格
        printNextOne(g);
//        绘制游戏得分
        printScore(g);
//        绘制游戏状态
        printState(g);
    }

    private void printState(Graphics g) {
        if(game_state == PLAYING){
            g.drawString(show_state[PLAYING],550,660);
        }else if(game_state == PAUSE){
            g.drawString(show_state[PAUSE],550,660);
        }else if(game_state == GAMEOVER){
            g.drawString(show_state[GAMEOVER],550,660);
            g.setColor(Color.red);
            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,50));
            g.drawString("GAME OVER!",55,400);
        }
    }

    private void printScore(Graphics g) {
        g.setFont(new Font(Font.DIALOG,Font.BOLD,30));
        g.drawString("得分：" + totalScore,525,250);
        g.drawString("行数：" + totalLine,525,435);
    }

    private void printNextOne(Graphics g) {
        g.setFont(new Font(Font.DIALOG,Font.BOLD,20));
        g.drawString("下一个：",465,40);
//        获取下一个下落的四方格中的Cell数组
        Cell[] cells = nextOne.cells;
//        遍历Cell数组进行绘制
        for (Cell cell : cells) {
//            获取行和列在加上偏移量放到合适的位置
            int x = cell.getCol() * CELL_SIZE + 396;
            int y = cell.getRow() * CELL_SIZE + 25;
            g.drawImage(cell.getImage(),x,y,null);
        }
    }

    private void printCurrentOne(Graphics g) {
//        获取正在下落四方格中的Cell数组
        Cell[] cells = currentOne.cells;
//        遍历Cell数组进行绘制
        for (Cell cell : cells) {
//            获取每个小方格的行和列
            int x = cell.getCol() * CELL_SIZE;
            int y = cell.getRow() * CELL_SIZE;
            g.drawImage(cell.getImage(),x,y,null);
        }
    }

    private void printWall(Graphics g) {
//        因为游戏区域是一个二维数组，所以先遍历
        for(int i = 0; i < wall.length; i++){
            for(int j = 0; j < wall[i].length; j++){
//                x代表的是列数
                int x = j * CELL_SIZE;
//                y代表的是行数
                int y = i * CELL_SIZE;
//                获取当前数组位置的单元格对象
                Cell cell = wall[i][j];
//                然后进行判断，如果当前位置上没有单元格就绘制矩形，如果有就把单元格嵌入进去
                if(cell == null){
                    g.drawRect(x,y,CELL_SIZE,CELL_SIZE);
                }else{
                    g.drawImage(cell.getImage(),x,y,null);
                }

            }
        }
    }

    /**
     * 判断正在下落的四方格是否出界
     * @return 返回true代表出界，返回false代表未出界
     */
    public boolean outOfBounds(){
//        获取正在下落四方格的Cell数组
        Cell[] cells = currentOne.cells;
//        遍历Cell数组判断其中的每个小方格是否出界（初始化二维数组是18行9列）
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
//            如果行数小于零或者超过二维数组的行数减一，列数小于零或者超过二维数组中一维数组的个数减一就代表出界
            if(row < 0 || row > wall.length - 1 || col < 0 || col > wall[0].length - 1){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断四方格是否重合
     * @return 返回true表示重合了，返回false表示没重合
     */
    public boolean coincide(){
//        获取正在下落的四方格
        Cell[] cells = currentOne.cells;
//        遍历这个数组
        for (Cell cell : cells) {
//            获取行和列的值
            int row = cell.getRow();
            int col = cell.getCol();
//            判断如果二维数组中这个不等于空，就代表重了
            if(wall[row][col] != null){
                return true;
            }
        }
        return false;
    }

    /**
     * 四方格按键左移方法
     */
    public void moveLeftAction(){
//        让当前四方格左移
        currentOne.moveLeft();
//        如果移动之后越界了或者重合了，就还原原来的位置
        if(outOfBounds() || coincide()){
            currentOne.moveRight();
        }
    }

    /**
     * 四方格按键右移方法
     */
    public void moveRightAction(){
//        让当前四方格右移
        currentOne.moveRight();
//        如果右移之后越界了或者重合了，就还原到原来的位置
        if(outOfBounds() || coincide()){
            currentOne.moveLeft();
        }
    }


    //    创建游戏场景
    public static void main(String[] args) {
//        创建一个窗口对象
        JFrame frame = new JFrame("俄罗斯方块");
//        创建游戏界面（面板）
        Tetris panel = new Tetris();
//        把游戏界面嵌入到窗口中
        frame.add(panel);
//        设置窗口可见
        frame.setVisible(true);
//        设置窗口大小
        frame.setSize(810,940);
//        设置居中显示
        frame.setLocationRelativeTo(null);
//        设置窗口关闭时程序终止
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        panel.start();
    }

}
