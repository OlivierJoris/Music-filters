import be.uliege.montefiore.oop.audio.*;

/**
 * Implementation of the CompositeOut class which represents an output of a
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
	 * Returns the number of inputs.
	 *
	 * @return The number of inputs.
	*/
	public int nbInputs(){ return 1;}

	/**
	 * Returns the number of outputs.
	 *
	 * @return The number of outputs.
	*/
	public int nbOutputs(){ return 1;}

	/**
     * Performs one step of computation of the CompositeOut.
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
     * Resets the CompositeOut
    */
	public void reset(){ return;}

}
