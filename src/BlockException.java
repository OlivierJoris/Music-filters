/**
 * Implementation of the BlockException class which represents an error in Block.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
class BlockException extends Exception{

	public BlockException(){ super();}

	public BlockException(String errorMessage){ super(errorMessage);}
}
