package net.sevecek.util;

public class ApplicationPublicException extends RuntimeException {

    private String errorCode;
    private Object[] errorMessageParameters;


    public ApplicationPublicException(String errorCode, Object... errorMessageParameters) {
        super(ExceptionUtils.formatErrorMessageForLoggingButNotForUserUI(errorCode, errorMessageParameters));
        this.errorCode = errorCode;
        this.errorMessageParameters = errorMessageParameters;
    }


    public ApplicationPublicException(Throwable cause, String errorCode, Object... errorMessageParameters) {
        super(ExceptionUtils.formatErrorMessageForLoggingButNotForUserUI(errorCode, errorMessageParameters), cause);
        this.errorCode = errorCode;
        this.errorMessageParameters = errorMessageParameters;
    }


    public String getErrorCode() {
        return errorCode;
    }


    public Object[] getErrorMessageParameters() {
        return errorMessageParameters;
    }


    /**
     * @deprecated Don't use this version of the constructor
     *             because it could easily be mis-interpreted
     *             as the other constructor
     *             {@link #ApplicationPublicException(String errorCode, Object... errorMessageParameters)}.<br/>
     *             Use ApplicationPublicException(Throwable cause, String errorCode) instead.
     */
    @Deprecated
    public ApplicationPublicException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @deprecated Don't call this method. It is here only for backwards compatibility.
     * It doesn't respect user's Locale.
     * Also it doesn't resolve error codes against a ResourceBundle.
     */
    @Deprecated
    @Override
    public String getMessage() {
        return super.getMessage();
    }


    /**
     * @deprecated Don't use this method. Problem is the same as in {@link #getMessage()}
     */
    @Deprecated
    @Override
    public String toString() {
        return super.toString();
    }

}
