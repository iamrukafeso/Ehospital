package com.ehospital;

public class Members {

    //Create members attributes
    private String memberId;
    private String fName;
    private String surname;
    private String email;
    private String pwd;
    private String dob;
    private String accountType;
    public Members(){

    }

    public Members(String memberId, String fName, String surname, String email, String pwd, String dob,String accountType) {
        this.memberId = memberId;
        this.fName = fName;
        this.surname = surname;
        this.email = email;
        this.pwd = pwd;
        this.dob = dob;
        this.accountType = accountType;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
