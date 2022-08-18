package demo1;

public class T extends Tetromino{

//    无参初始化T“O O O”
//                O

    public T() {
        cells[0] = new Cell(0,4,Tetris.T);
        cells[1] = new Cell(0,3,Tetris.T);
        cells[2] = new Cell(0,5,Tetris.T);
        cells[3] = new Cell(1,4,Tetris.T);


//        该图形有四种旋转状态
        states = new State[4];
//        初始化四种旋转状态的相对坐标
        states[0] = new State(0,0,0,-1,0,1,1,0);
        states[1] = new State(0,0,-1,0,1,0,0,-1);
        states[2] = new State(0,0,0,1,0,-1,-1,0);
        states[3] = new State(0,0,1,0,-1,0,0,1);
    }
}
