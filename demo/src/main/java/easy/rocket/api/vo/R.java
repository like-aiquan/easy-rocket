package easy.rocket.api.vo;

/**
 * @author chenaiquan
 * @date 2022/6/14 19:49
 */
public class R {
  private int code;
  private String message;
  private Object data;

  private R code(int code) {
    this.code = code;
    return this;
  }

  private R message(String message) {
    this.message = message;
    return this;
  }

  private <T> R data(T data) {
    this.data = data;
    return this;
  }

  public static R success() {
    return success(null);
  }

  public static R success(Object data) {
    return new R().code(200).message("success!").data(data);
  }

  public static R error() {
    return error("service error!");
  }

  public static R error(String message) {
    return error(500, message);
  }

  public static R error(int code, String message) {
    return error(code, message, null);
  }

  public static R error(int code, String message, Object data) {
    return new R().code(code).message(message).data(data);
  }

  @Deprecated
  public int getCode() {
    return code;
  }

  @Deprecated
  public void setCode(int code) {
    this.code = code;
  }

  @Deprecated
  public String getMessage() {
    return message;
  }

  @Deprecated
  public void setMessage(String message) {
    this.message = message;
  }

  @Deprecated
  public Object getData() {
    return data;
  }

  @Deprecated
  public void setData(Object data) {
    this.data = data;
  }
}
