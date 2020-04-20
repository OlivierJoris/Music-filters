/**
 * Implementation of the CompositeFilterException class which represents an
 * error in CompositeFilter.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public class CompositeFilterException extends Exception{

	/**
	 * Constructor without paramaters.
	*/
	public CompositeFilterException(){ super();}

	/**
	 * Constructor with a parameter.
	 *
	 * @param errorMessage An error message.
	*/
	public CompositeFilterException(String errorMessage){ super(errorMessage);}
}
