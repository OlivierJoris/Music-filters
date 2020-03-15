/* ------------------------------------------------------------------------- *
 * Implementation of the DelayFilter class : Filter subclass.
 * 
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

public class DelayFilter extends Filter
{
    private int delayValue;

    /* ------------------------------------------------------------------------- *
     * Constructor method.
     * 
     * @param nbInputs, the number of inputs of the DelayFilter.
     * @param nbOutputs, the number of outputs of the DelayFilter.
     * @param delayValue, the delay value of the DelayFilter.
     * 
     * @throws, the number of inputs is not equal to the number of outputs.
     * ------------------------------------------------------------------------- */
    public DelayFilter(int nbInputs, int nbOutputs, int delayValue)
    {   
        // TODO: WHEN WE'LL HAVE SEEN THE THROW EXCEPTION
        // if(nbInputs != nbOutputs)
            //Throw Exception

        super(nbInputs, nbOutputs);
        this.delayValue = delayValue;
    }

    /* ------------------------------------------------------------------------- *
     * Getter method for delayValue.
     * 
     * @return delayValue, the delayValue of the DelayFilter.
     * ------------------------------------------------------------------------- */
    public int delayValue()
    {
        return delayValue;
    }

    /* ------------------------------------------------------------------------- *
     * Perfoms one step of computation of the DelayFilter.
     *
     * @param input, an array containing n_I samples (one for each input).
     * 
     * @throws FilterException, inputs are incomplete.
     * 
     * @return, an array with the resulting n_O samples (one for each output).
     * ------------------------------------------------------------------------- */
    public double[] computeOneStep(double[] input)
    {
        // TODO : WHEN WE'LL HAVE SEEN THE THROW EXCEPTIONS
        // if(input.length != nbInputs())
            //Throw Exception (FilterException)

        double[] output = new double[nbOutputs()]; 

        for(int i = 0; i < nbInputs() - delayValue; ++i)
            output[i + delayValue] = input[i];

        return output;
    }
}
