/* ------------------------------------------------------------------------- *
 * Implementation of the CompositeFilterException class which represents an
 * error in CompositeFilter.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

class CompositeFilterException extends Exception{

	public CompositeFilterException(){ super();}

	public CompositeFilterException(String errorMessage){ super(errorMessage);}
}
