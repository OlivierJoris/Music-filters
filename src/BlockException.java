/**
 * Implementation of the BlockException class which represents an error in Block.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public class BlockException extends Exception{

	/**
	 * Constructor without parameters.
	*/
	public BlockException(){ super();}

	/**
	 * Constructor with a parameter.
	 *
	 * @param errorMessage An error message.
	*/
	public BlockException(String errorMessage){ super(errorMessage);}
}
