package railroad.exceptions;

/**
 * Exception class is raised when for some reason car cannot be attached to trainset
 */
public class CannotAttachException extends Exception{
    public CannotAttachException(String s) {
        super(s);
    }
}
