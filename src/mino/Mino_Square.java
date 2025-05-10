package mino;
import java.awt.Color;

public class Mino_Square extends Mino {
    public Mino_Square(){
        create(Color.yellow);
    }

    public void setXY(int x, int y){
        // *  *
        // *  *
        b[0].x = x; // đây là mảng block , khối mino được cấu tạo từ 4 block
        b[0].y = y;// nen có kí hiệu là b[0], b[1], b[2], b[3]
        b[1].x = b[0].x;
        b[1].y = b[0].y + Block.Size; //.y là tọa độ theo chiều dọc của block
        b[2].x = b[0].x + Block.Size; // y = 0 nằm trên đỉnh màn hình, còn y càng lớn thì block nam càng thấp
        b[2].y = b[0].y;
        b[3].x = b[0].x + Block.Size;
        b[3].y = b[0].y + Block.Size;
    }

    public void getDirection1(){}
    public void getDirection2(){}
    public void getDirection3(){}
    public void getDirection4(){}
}
 //b[2].x = b[0].x - Block.Size;, Nghĩa là: đặt block b[2] lùi sang trái một block so với b[0].
//b[2]: Block thứ ba trong khối.
//.x: Tọa độ theo trục ngang (x).
//x = 0 là bên trái màn hình.
//
//x càng lớn thì block càng nằm về bên phải.