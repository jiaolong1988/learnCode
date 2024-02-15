package designPatterns.Template;

import java.io.Console;

/**
 * @author jiaolong
 * @date 2024/01/19 14:01
 * @description: ģ�巽������
 */
public class TemplateExample {
    public static void main(String[] args) {
        System.out.println("ѧ���׳����Ծ�");
        TestPaperA studentA = new TestPaperA();
        studentA.TestQuestion1();
        studentA.TestQuestion2();
        studentA.TestQuestion3();

        System.out.println("\nѧ���ҳ����Ծ�");
        TestPaperB studentB = new TestPaperB();
        studentB.TestQuestion1();
        studentB.TestQuestion2();
        studentB.TestQuestion3();

    }
}

abstract class TestPaper {
    public void TestQuestion1() {
        System.out.println(" ����õ����������˹������������콣��������������������[ ] a.��ĥ���� b.����� c.���ٺϽ�� d.̼����ά ");
        //ģ�巽���Ĺؼ�
        System.out.println(" �𰸣�"+answer1());
    }


    public void TestQuestion2() {
        System.out.println(" �������Ӣ��½��˫�������黨�����[ ] a.ʹ����ֲ�ﲻ�ٺ��� b.ʹһ����ϡ������� c.�ƻ����Ǹ�����Ȧ����̬ƽ�� d.��ɸõ���ɳĮ��");
        //ģ�巽���Ĺؼ�
        System.out.println(" �𰸣�"+answer2());
    }

    public void TestQuestion3() {
        System.out.println(" �������ʹ��ɽʦͽ���ҹ�����Ż�²�ֹ��������Ǵ�򣬻�����ǿ�ʲôҩ[ ]a.��˾ƥ�� b.ţ�ƽⶾƬ c.������ d.�����Ǻȴ�������ţ�� e.����ȫ����");
        //ģ�巽���Ĺؼ�
        System.out.println(" �𰸣�"+answer3());
    }

    abstract String answer1();
    abstract String answer2();
    abstract String answer3();
}

//ѧ���׳����Ծ�
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
//        System.out.println("�𰸣�b");
//    }
//
//    @Override
//    public void TestQuestion2() {
//        super.TestQuestion2();
//        System.out.println("�𰸣�b");
//    }
//
//    @Override
//    public void TestQuestion3() {
//        super.TestQuestion3();
//        System.out.println("�𰸣�b");
//    }
}

//ѧ���׳����Ծ�
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
//        System.out.println("�𰸣�c");
//    }
//
//    @Override
//    public void TestQuestion2() {
//        super.TestQuestion2();
//        System.out.println("�𰸣�c");
//    }
//
//    @Override
//    public void TestQuestion3() {
//        super.TestQuestion3();
//        System.out.println("�𰸣�c");
//    }

}