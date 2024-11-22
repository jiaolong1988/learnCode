package designPatterns.Template;

import java.io.Console;

/**
 * @author jiaolong
 * @date 2024/01/19 14:01
 * @description: 模板方法案例
 */
public class TemplateExample {
    public static void main(String[] args) {
        System.out.println("学生甲抄的试卷：");
        TestPaperA studentA = new TestPaperA();
        studentA.TestQuestion1();
        studentA.TestQuestion2();
        studentA.TestQuestion3();

        System.out.println("\n学生乙抄的试卷：");
        TestPaperB studentB = new TestPaperB();
        studentB.TestQuestion1();
        studentB.TestQuestion2();
        studentB.TestQuestion3();

    }
}

abstract class TestPaper {
    public void TestQuestion1() {
        System.out.println(" 杨过得到，后来给了郭靖，炼成倚天剑、屠龙刀的玄铁可能是[ ] a.球磨铸铁 b.马口铁 c.高速合金钢 d.碳素纤维 ");
        //模板方法的关键
        System.out.println(" 答案："+answer1());
    }


    public void TestQuestion2() {
        System.out.println(" 杨过、程英、陆无双铲除了情花，造成[ ] a.使这种植物不再害人 b.使一种珍稀物种灭绝 c.破坏了那个生物圈的生态平衡 d.造成该地区沙漠化");
        //模板方法的关键
        System.out.println(" 答案："+answer2());
    }

    public void TestQuestion3() {
        System.out.println(" 蓝凤凰致使华山师徒、桃谷六仙呕吐不止，如果你是大夫，会给他们开什么药[ ]a.阿司匹林 b.牛黄解毒片 c.氟哌酸 d.让他们喝大量的生牛奶 e.以上全不对");
        //模板方法的关键
        System.out.println(" 答案："+answer3());
    }

    abstract String answer1();
    abstract String answer2();
    abstract String answer3();
}

//学生甲抄的试卷
class TestPaperA extends TestPaper {
    @Override
    String answer1() {
        return "a";
    }

    @Override
    String answer2() {
        return "a";
    }

    @Override
    String answer3() {
        return "a";
    }
    //    @Override
//    public void TestQuestion1() {
//        super.TestQuestion1();
//        System.out.println("答案：b");
//    }
//
//    @Override
//    public void TestQuestion2() {
//        super.TestQuestion2();
//        System.out.println("答案：b");
//    }
//
//    @Override
//    public void TestQuestion3() {
//        super.TestQuestion3();
//        System.out.println("答案：b");
//    }
}

//学生甲抄的试卷
class TestPaperB extends TestPaper {
    @Override
    String answer1() {
        return "b";
    }

    @Override
    String answer2() {
        return "b";
    }

    @Override
    String answer3() {
        return "b";
    }

//    @Override
//    public void TestQuestion1() {
//        super.TestQuestion1();
//        System.out.println("答案：c");
//    }
//
//    @Override
//    public void TestQuestion2() {
//        super.TestQuestion2();
//        System.out.println("答案：c");
//    }
//
//    @Override
//    public void TestQuestion3() {
//        super.TestQuestion3();
//        System.out.println("答案：c");
//    }

}