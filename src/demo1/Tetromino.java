package demo1;

import java.util.Objects;
import java.util.Random;

/*
该类是四方格的父类，因为每个俄罗斯方块都是由四个方格组成的
    属性：Cell数组，并且数组的初始化容量为4
    方法：左移一格、右移一格、下落一格、（变形）
    注意：整个图形移动代表着里面所有的小方格移动
 */
public class Tetromino {

//    属性：四个小方格
    protected Cell[] cells = new Cell[4];
//    属性：旋转状态
    protected State[] states;
//    属性：旋转次数
    protected int count = 12000;

    /**
     * 顺时针旋转四方格
     */
    public void retateRight(){
//        先进行判断，如果旋转状态不为零，进行旋转
        if(states.length == 0){
            return;
        }
        //旋转状态加1
        count++;
//        这个得到是旋转之后的四方块属于哪种状态
        State state = states[count % states.length];
//        获取这个四方格的起点小方块
        Cell cell = cells[0];
//        获取起点的行列值
        int row = cell.getRow();
        int col = cell.getCol();
//        用行列偏移达到旋转的目的(因为之前的状态坐标是相对的)
        cells[1].setRow(row + state.row1);
        cells[1].setCol(col + state.col1);
        cells[2].setRow(row + state.row2);
        cells[2].setCol(col + state.col2);
        cells[3].setRow(row + state.row3);
        cells[3].setCol(col + state.col3);
    }

    /**
     * 逆时针旋转四方格
     */
    public void retateLeft(){
//        先进行判断，如果旋转状态不为零，进行旋转
        if(states.length == 0){
            return;
        }
        //旋转状态减1
        count--;
//        这个得到是旋转之后的四方块属于哪种状态
        State state = states[count % states.length];
//        获取这个四方格的起点小方块
        Cell cell = cells[0];
//        获取起点的行列值
        int row = cell.getRow();
        int col = cell.getCol();
//        用行列偏移达到旋转的目的(因为之前的状态坐标是相对的)
        cells[1].setRow(row + state.row1);
        cells[1].setCol(col + state.col1);
        cells[2].setRow(row + state.row2);
        cells[2].setCol(col + state.col2);
        cells[3].setRow(row + state.row3);
        cells[3].setCol(col + state.col3);
    }

//    编写四方格旋转状态内部类
    class State{
//        属性：四方格各元素的相对位置
        int row0,col0,row1,col1,row2,col2,row3,col3;

    public State() {
    }

    public State(int row0, int col0, int row1, int col1, int row2, int col2, int row3, int col3) {
        this.row0 = row0;
        this.col0 = col0;
        this.row1 = row1;
        this.col1 = col1;
        this.row2 = row2;
        this.col2 = col2;
        this.row3 = row3;
        this.col3 = col3;
    }

    public int getRow0() {
        return row0;
    }

    public void setRow0(int row0) {
        this.row0 = row0;
    }

    public int getCol0() {
        return col0;
    }

    public void setCol0(int col0) {
        this.col0 = col0;
    }

    public int getRow1() {
        return row1;
    }

    public void setRow1(int row1) {
        this.row1 = row1;
    }

    public int getCol1() {
        return col1;
    }

    public void setCol1(int col1) {
        this.col1 = col1;
    }

    public int getRow2() {
        return row2;
    }

    public void setRow2(int row2) {
        this.row2 = row2;
    }

    public int getCol2() {
        return col2;
    }

    public void setCol2(int col2) {
        this.col2 = col2;
    }

    public int getRow3() {
        return row3;
    }

    public void setRow3(int row3) {
        this.row3 = row3;
    }

    public int getCol3() {
        return col3;
    }

    public void setCol3(int col3) {
        this.col3 = col3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return row0 == state.row0 &&
                col0 == state.col0 &&
                row1 == state.row1 &&
                col1 == state.col1 &&
                row2 == state.row2 &&
                col2 == state.col2 &&
                row3 == state.row3 &&
                col3 == state.col3;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row0, col0, row1, col1, row2, col2, row3, col3);
    }

    @Override
    public String toString() {
        return "State{" +
                "row0=" + row0 +
                ", col0=" + col0 +
                ", row1=" + row1 +
                ", col1=" + col1 +
                ", row2=" + row2 +
                ", col2=" + col2 +
                ", row3=" + row3 +
                ", col3=" + col3 +
                '}';
    }
}
    /**
     * 图形左移
     */
    public void moveLeft(){
        for (Cell cell : cells){
            cell.left();
        }
    }

    /**
     * 图形右移
     */
    public void moveRight(){
        for (Cell cell : cells){
            cell.right();
        }
    }

    /**
     * 图形下落
     */
    public void softDrop(){
        for (Cell cell : cells){
            cell.drop();
        }
    }

    /**
     * 生成随机形状的四方格
     * @return 返回随机生成的这个四方格
     */
    public static Tetromino randomOne(){
        Random random = new Random();
        int num = random.nextInt(7);
        Tetromino tetromino = null;
        switch(num){
            case 0:
                tetromino = new I();
                break;
            case 1:
                tetromino = new J();
                break;
            case 2:
                tetromino = new L();
                break;
            case 3:
                tetromino = new O();
                break;
            case 4:
                tetromino = new S();
                break;
            case 5:
                tetromino = new T();
                break;
            case 6:
                tetromino = new Z();
                break;
        }
        return tetromino;
    }
}
