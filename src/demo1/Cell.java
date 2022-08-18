package demo1;

import java.awt.image.BufferedImage;
import java.util.Objects;

/*
俄罗斯方块都是由一个小方块组成的，所有先实现一个小方块类
    这个类应该有：
        属性：行、列、每个小方块中的填充图片
        方法：左移一格、右移一格、下落一格
 */
public class Cell {

//    属性行
    private int row;
//    属性列
    private int col;
//    属性填充图片
    private BufferedImage image;
//    Constructor

    public Cell() {
    }

    public Cell(int row, int col, BufferedImage image) {
        this.row = row;
        this.col = col;
        this.image = image;
    }
//    Setter and Getter

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
//    重写equals and toString

    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", col=" + col +
                ", image=" + image +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
                col == cell.col &&
                Objects.equals(image, cell.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, image);
    }
//    左移一格方法
    public void left(){
        col--;
    }

//    右移一格方法
    public void right(){
        col++;
    }

//    下降一格方法
    public void drop(){
        row++;
    }
}
