package example.calcolatrice.expression;

public class ExpressionException extends Exception{
    private String messageDetail;

    public ExpressionException(String message, String messageDetail) {
        super(message);
        this.messageDetail = messageDetail;
    }

    public String getMessageDetail() {return messageDetail;}
}
