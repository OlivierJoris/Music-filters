/* ------------------------------------------------------------------------- *
 * Implementation of the ParametersException class which represents an
 * error in the given command-line parameters.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

class ParametersException extends Exception{

	String errorMessage;

	public ParametersException(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String toString()
	{
		return errorMessage;
	}
}
