/* ------------------------------------------------------------------------- *
 * Implementation of the GainFilter class : Filter subclass (specialization).
 * 
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

public class GainFilter extends Filter
{
    private double gainValue;

    /* ------------------------------------------------------------------------- *
     * Constructor method.
     * 
     * @param nbInputs, the number of inputs of the GainFilter.
     * @param nbOutputs, the number of outputs of the GainFilter.
     * @param gainValue, the gain value of the GainFilter.
     * 
     * @throws, the number of inputs is not equal to the number of outputs.
     * ------------------------------------------------------------------------- */
    public GainFilter(int nbInputs, int nbOutputs, double gainValue)
    {   
        // TODO: WHEN WE'LL HAVE SEEN THE THROW EXCEPTION
        // if(nbInputs != nbOutputs)
            //Throw Exception

        super(nbInputs, nbOutputs);
        this.gainValue = gainValue;
    }

    /* ------------------------------------------------------------------------- *
     * Getter method for GainValue.
     * 
     * @return gainValue, the gainValue of the GainFilter.
     * ------------------------------------------------------------------------- */
    public double gainValue()
    {
        return gainValue;
    }

    /* ------------------------------------------------------------------------- *
     * Perfoms one step of computation of the Gainfilter.
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

        for(int i = 0; i < nbInputs(); ++i)
            output[i] = input[i] * gainValue;

        return output;
    }
}
