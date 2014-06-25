package la.service.common;

public class ReqNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ReqNotFoundException() {
        super();
    }

    public ReqNotFoundException(String message) {
        super(message);
    }
}