package io;

public class AutoCloseIOStreamTest {
    public static void main(String[] args) {

        try (MyResource mr = new MyResource()) {
            System.out.println("1-MyResource created in try-with-resources");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("3-Out of try-catch block.");
    }

    static class MyResource implements AutoCloseable{

        @Override
        public void close() throws Exception {
            System.out.println("2-Closing MyResource");
        }

    }
}
