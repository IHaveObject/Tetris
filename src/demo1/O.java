package demo1;

public class O extends Tetromino{

//    无参构造O：O O
//             O O
    public O(){
        cells[0] = new Cell(0,4,Tetris.O);
        cells[1] = new Cell(0,5,Tetris.O);
        cells[2] = new Cell(1,4,Tetris.O);
        cells[3] = new Cell(1,5,Tetris.O);

//        该图形有零种旋转状态
        states = new State[0];
    }
}
