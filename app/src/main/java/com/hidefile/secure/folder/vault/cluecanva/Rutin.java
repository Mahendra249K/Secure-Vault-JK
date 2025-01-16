package com.hidefile.secure.folder.vault.cluecanva;

public class Rutin {

    public static final String ABLAME = "tabUser";
    public static final String MNID = "id";
    public static final String CLPWD = "passwrod";
    public static final String CLMSQCM = "squestion";
    public static final String CLNANS = "answer";
    public static final String CLMAIL = "email";
    public static final String CLMFPIN = "forgot_pin";
    public static final String MAKETBL = "CREATE TABLE " + ABLAME + "(" + MNID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CLPWD + " TEXT," + CLMSQCM + " TEXT," + CLNANS + " TEXT," + CLMAIL + " TEXT," + CLMFPIN + " TEXT" + ")";
    public static final String STRING1 = "ALTER TABLE " + Rutin.ABLAME + " ADD COLUMN " + Rutin.CLMAIL + " TEXT";
    public static final String STRING2 = "ALTER TABLE " + Rutin.ABLAME + " ADD COLUMN " + Rutin.CLMFPIN + " TEXT";
    public int id;
    public String pwd;
    public String squestion;
    public String answer;
    public String email;
    public String forgotPin;
    public Rutin(int id, String pwd, String squestion, String answer, String email, String forgotPin) {
        this.id = id;
        this.pwd = pwd;
        this.squestion = squestion;
        this.answer = answer;
        this.email = email;
        this.forgotPin = forgotPin;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getSquestion() {
        return squestion;
    }
    public void setSquestion(String squestion) {
        this.squestion = squestion;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getForgotPin() {
        return forgotPin;
    }
    public void setForgotPin(String forgotPin) {
        this.forgotPin = forgotPin;
    }
    @Override
    public String toString() {
        return "Rutin{" +
                "id=" + id +
                ", pwd='" + pwd + '\'' +
                ", squestion='" + squestion + '\'' +
                ", answer='" + answer + '\'' +
                ", email='" + email + '\'' +
                ", forgotPin='" + forgotPin + '\'' +
                '}';
    }
}
