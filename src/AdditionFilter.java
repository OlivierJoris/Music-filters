/* ------------------------------------------------------------------------- *
 * Implementation of the AdditionFilter class.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import be.uliege.montefiore.oop.audio.Filter;
import be.uliege.montefiore.oop.audio.FilterException;

public class AdditionFilter implements Filter
{
    /* ------------------------------------------------------------------------- *
     * Constructor method.
     * ------------------------------------------------------------------------- */
    public AdditionFilter()
    {
        return;
    }

    /*
    * Implementation of the nbInputs() and nbOutputs() methods : An
    AdditionFilter has 2 inputs and 1 output.
    */
	public int nbInputs()
	{
    	return 2;
	}

	public int nbOutputs()
	{
    	return 1;
   	}

    /* ------------------------------------------------------------------------- *
     * Perfoms one step of computation of the AdditionFilter.
     *
     * @param input, an array containing n_I samples (one for each input).
     *
     * @throws FilterException, inputs are incomplete.
     *
     * @return, an array with the resulting n_O samples (one for each output).
     * ------------------------------------------------------------------------- */
    public double[] computeOneStep(double[] input) throws FilterException
    {
        if(input.length != nbInputs())
            throw new FilterException("Inputs are incomplete");

        System.out.println(input.length);

        double[] output = new double[1];

        output[0] = input[0] + input[1];

        return output;
    }

	public void reset()
	{
    	return;
	}
}
