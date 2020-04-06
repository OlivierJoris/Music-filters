import be.uliege.montefiore.oop.audio.*;

/**
 * Implementation of the CompositeOut class which represents an output of an
 * CompositeFilter.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public class CompositeOut implements Filter
{
	/**
	 * Constructor method
	*/
	public CompositeOut(){ return;}

	/**
	 * Returns the number of inputs
	*/
	public int nbInputs(){ return 1;}

	/*
	 * Returns the number of outputs
	*/
	public int nbOutputs(){ return 1;}

	/**
     * Perfoms one step of computation of the CompositeIn.
     *
     * @param input An array containing n_I samples (one for each input).
     *
     * @throws FilterException Inputs are incomplete.
     *
     * @return An array with the resulting n_O samples (one for each output).
    */
	public double[] computeOneStep(double[] input) throws FilterException
	{
		return null;
	}

	/**
     * Reset the CompositeIn
    */
	public void reset(){ return;}

}
