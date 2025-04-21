package stream.eg;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * list 转换为map数组
 * @author jiaolong
 * @date 2023-03-01 03:28:54
 */
public class ListToMap {
   static class User {
        private int id;
        private String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }


    public static void main(String[] args) {

        List<User> users = Arrays.asList(
                new User(1, "Alice"),
                new User(2, "Bob"),
                new User(1, "Alice Updated"), // ID 重复但内容不同
                new User(3, "Charlie")
        );

        Map<Integer, List<User>> userMap1 = users.stream()
                .collect(Collectors.groupingBy(User::getId));
        System.out.println(userMap1);


        Map<Integer, String> userMap2 = users.stream()
                .collect(Collectors.toMap(
                        User::getId, // 键：用户 ID
                        User::getName, // 值：用户姓名
                        (existing, replacement) -> existing + " & " + replacement // 合并函数
                ));
        System.out.println(userMap2);
    }

}
