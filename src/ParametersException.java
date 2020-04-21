/**
 * Implementation of the ParametersException class which represents an
 * error in the given command-line parameters.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public class ParametersException extends Exception
{
	/**
	 * Constructor without parameters.
	*/
	public ParametersException() { super();}

	/**
	 * Constructor with a parameter.
	 *
	 * @param errorMessage An error message.
	*/
	public ParametersException(String errorMessage) { super(errorMessage);}
}
