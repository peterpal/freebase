package sk.fiit.stuba.ir.badal.matcher;

/**
 * Created by delve on 11/10/14.
 */
public class Result {

    public static int CODE_MET = 100;
    public static int CODE_NOT_MET = 200;
    public static int CODE_MAYBE = 300;

    private String message;
    private int code;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "[" + this.message + ", " + this.code + "]";
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
