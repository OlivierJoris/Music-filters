import be.uliege.montefiore.oop.audio.Filter;
import be.uliege.montefiore.oop.audio.FilterException;

/**
 * Implementation of the GainFilter class.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113)
*/
public class GainFilter implements Filter
{
    private double gainValue; // The gain value of the GainFilter.

    /**
     * Constructor method.
     *
     * @param gainValue The gain value of the GainFilter.
	 * @throws FilterException The gainValue can't be smaller than 0.
    */
    public GainFilter(double gainValue) throws FilterException
    {
		if(gainValue < 0)
			throw new FilterException("gainValue can't be a negative number");

        this.gainValue = gainValue;
    }

	/**
	 * Returns the number of inputs.
	 *
	 * @return The number of inputs.
	*/
   	public int nbInputs()
   	{
    	return 1;
   	}

	/**
	 * Returns the number of outputs.
	 *
	 * @return The number of outputs.
	*/
   	public int nbOutputs()
   	{
    	return 1;
   	}

    /**
     * Performs one step of computation of the Gainfilter.
     *
     * @param input An array containing n_I samples (one for each input).
     *
     * @throws FilterException Inputs are incomplete.
     *
     * @return An array with the resulting n_O samples (one for each output).
    */
    public double[] computeOneStep(double[] input) throws FilterException
    {
        if(input.length != nbInputs())
            throw new FilterException("Invalid number of inputs");

        double[] output = new double[1];

        output[0] = input[0] * gainValue;

        return output;
    }

	/**
     * Resets the GainFilter
    */
    public void reset()
    {
        // We do not need to reset anything.
        return;
    }
}
