package model;

/**
 * @author: jiaolong
 * @date: 2024/06/17 10:10
 **/
public class Employee {
    private int id;
    private String gender;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
