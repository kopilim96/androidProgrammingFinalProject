package info.androidhive.kopilim;

public class Employee {
    private String fullName;
    private String address;
    private String sex;
    private String icNum;
    private String phoneNum;
    private String id;
    private String email;

    public Employee(){
        //Empty
    }

    public Employee(String id, String fullName, String icNum, String sex, String phoneNum, String address, String email) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.sex = sex;
        this.icNum = icNum;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIcNum() {
        return icNum;
    }

    public void setIcNum(String icNum) {
        this.icNum = icNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
