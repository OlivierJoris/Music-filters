import be.uliege.montefiore.oop.audio.Filter;
import be.uliege.montefiore.oop.audio.FilterException;

/**
 * Implementation of the delay filter.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113)
*/
public class DelayFilter implements Filter
{
	private int delayValue; // The delay value of the DelayFilter.
    private int count; // A counter for delaying the samples.
    private double[] delayedSamples; /* An array to memorize the actual
                                        delayed samples */

    /**
     * Constructor method.
     *
     * @param delayValue The delay value of delay.
	 *
	 * @throws FilterException delayValue can't be smaller than 0.
    */
    public DelayFilter(int delayValue) throws FilterException
    {
		if(delayValue < 0)
			throw new FilterException("delayValue can't be smaller than 0.");

        this.delayValue = delayValue;

        count = 0;

        delayedSamples = new double[delayValue];

        // We fill the array with zeros in order to delay the samples.
        for(int i = 0; i < delayedSamples.length; ++i)
            delayedSamples[i] = 0.0;

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
     * Performs one step of computation of the DelayFilter.
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

        if(count == delayValue)
            count = 0;

        output[0] = delayedSamples[count];
        delayedSamples[count] = input[0];

        ++count;

        return output;
    }

    /**
     * Resets the DelayFilter.
    */
	public void reset()
    {
        count = 0;
		
        // We reset the delayedSamples array.
        for(int i = 0; i < delayedSamples.length; ++i)
            delayedSamples[i] = 0.0;
    }
}
