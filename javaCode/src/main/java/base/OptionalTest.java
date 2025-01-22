package base;

import java.util.Optional;

/**
 * @author jiaolong
 * @date 2025/01/22 16:12
 **/
public class OptionalTest {

    public static void main(String[] args) {
        //方式1
        Zoo zoo = new Zoo();
        zoo.setDog(new Dog(1));
        if(zoo != null){
            Dog dog = zoo.getDog();
            if(dog != null){
                int age = dog.getAge();
                System.out.println("way1: "+age);
            }
        }

        //方式2
        Optional.ofNullable(zoo).map(o -> o.getDog()).map(d -> d.getAge()).ifPresent(age ->
                System.out.println("way2: "+age)
        );

        //方式3:值不存在，返回默认值
        String testValue = null;
        String getVal = Optional.ofNullable(testValue).map(x->x).filter(x-> true).orElse("不存在");
        System.out.println("way3: "+getVal);

    }
}

class Zoo {
    private Dog dog;

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

}

class Dog {
    private int age;
    Dog(int x){
        this.age=x;
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}