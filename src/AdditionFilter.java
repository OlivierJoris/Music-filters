import be.uliege.montefiore.oop.audio.Filter;
import be.uliege.montefiore.oop.audio.FilterException;

/**
* Implementation of the AdditionFilter class
* @author Maxime GOFFART (180521) and Olivier JORIS (182113)
*/
public class AdditionFilter implements Filter
{
    /**
     * Constructor method.
    */
    public AdditionFilter()
    {
        return;
    }

    /**
	 * Returns the number of inputs
    */
	public int nbInputs()
	{
    	return 2;
	}

	/**
	 * Returns the number of outputs
	*/
	public int nbOutputs()
	{
    	return 1;
   	}

    /**
     * Perfoms one step of computation of the AdditionFilter
     *
     * @param input An array containing n_I samples (one for each input)
     *
     * @throws FilterException Inputs are incomplete
     *
     * @return An array with the resulting n_O samples (one for each output)
    */
    public double[] computeOneStep(double[] input) throws FilterException
    {
        if(input.length != nbInputs())
            throw new FilterException("Inputs are incomplete");

        //System.out.println(input.length);

        double[] output = new double[1];

        output[0] = input[0] + input[1];

        return output;
    }

	/**
	 * Reset the Filter
	*/
	public void reset()
	{
    	return;
	}
}
