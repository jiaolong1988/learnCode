package base.enumtest;

import java.util.Date;

/**
 * @author jiaolong
 * @date 2025/01/20 10:24
 **/
public enum Planet {
//	A,B;

    AAA(1, 2),
    BBB(3, 4);


    private int a;
    private int b;

    Planet(int a, int b) {
        this.a = a;
        this.b = b;
    }

    String getmessage(String flag, int a) {
        Date date = new Date();

        System.out.println(flag + ": get message :" + System.currentTimeMillis() + " a:" + a);
        return date.toString();
    }


    public static void main(String[] args) throws InterruptedException {
        //获取所有枚举
        for (Planet p : Planet.values()) {
            System.out.println(p);
        }
        System.out.println("===================");

        //调用枚举的方法
        for (int i = 0; i < 10; i++) {
            Planet.AAA.getmessage(Planet.AAA.toString(), Planet.AAA.a);
            Thread.sleep(2000);
            Planet.BBB.getmessage(Planet.BBB.toString(), Planet.BBB.a);
        }

//    	for(int i=0;i<10;i++) {
//    		Planet.A.getmessage("A");
//    		Thread.sleep(2000);
//    		Planet.B.getmessage(B.toString());
//	    }

    }

}

