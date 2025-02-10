package generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 在形参与类定义时均可设置 通配符。
 *
 * @author jiaolong
 * @date 2025/02/10 14:31
 **/
public class GenericsSubClassUpDownTest {
    public static void main(String[] args) {
        //1.通配符的测试
        List<Circle> circleList = new ArrayList<>();
        circleList.add(new Circle());
        List<Rectangle> rectangleList = new ArrayList<>();
        rectangleList.add(new Rectangle());

        /**
         *  1.由于List<Circle>并不是List<Shape>的子类型,因此不能直接转化 List<Shape> s = circleList;
         *  2.<? extends Shape>表示Shape的任意一个子类的类型，因此List<? extends Shape> 只能以父类(Shape)的方式获取对象。
         */
        List<? extends Shape> shape1 = circleList;
        List<? extends Shape> shape2 = rectangleList;


        //2.形参上设置通配符
        Canvas c = new Canvas();
        c.drawAll(circleList);
        c.drawAll(rectangleList);

        /*
         * 3. 通配符类型的集合 【不能】添加对象，【只能】获取对象， 因为集合不能确定集合的具体类型。
         **/
        shape1.get(0);
        // shape2.add((Shape)new Circle());

        List<?> list = circleList;
        list.get(0);
        // list.add("");

    }
}

// 定义一个抽象类Shape[形状]
abstract class Shape {
    public abstract void draw(Canvas c);
}

//定义Shape的子类Circle[圆圈]
class Circle extends Shape {
    // 实现画图方法，以打印字符串来模拟画图方法实现
    public void draw(Canvas c) {
        System.out.println("Circle：" + c);
    }
}

//定义Shape的子类Rectangle[长方形]
class Rectangle extends Shape {
    // 实现画图方法，以打印字符串来模拟画图方法实现
    public void draw(Canvas c) {
        System.out.println("Rectangle：" + c);
    }
}

//[油画]
class Canvas {
    //使用被限制的泛型通配符
    public void drawAll(List<? extends Shape> shapes) {
        for (Shape s : shapes) {
            s.draw(this);

        }
    }
}