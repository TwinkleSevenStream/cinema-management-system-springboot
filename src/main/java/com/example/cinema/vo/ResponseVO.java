package     com.example.cinema.vo;

/*
 * 
 * 返回值类
 * 	返回执行某个操作后的状态信息：成功/失败， 提示信息 ， 内容。
 * 
 */
public class ResponseVO {

    /**
     * 调用是否成功
     */
    private Boolean success;

    /**
     * 返回的提示信息
     */
    private String message;

    /**
     * 内容
     */
    private Object content;

    public static ResponseVO buildSuccess(){
    	//这种编写真的好吗？创建一个自己，然后赋值，返回
        ResponseVO response=new ResponseVO();
        response.setSuccess(true);
        return response;
    }

    public static ResponseVO buildSuccess(Object content){
        ResponseVO response=new ResponseVO();
        response.setContent(content);
        response.setSuccess(true);
        return response;
    }

    public static ResponseVO buildFailure(String message){
        ResponseVO response=new ResponseVO();
        response.setSuccess(false);
        response.setMessage(message);
//        System.out.println(message);
        return response;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
