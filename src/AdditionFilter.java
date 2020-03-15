/* ------------------------------------------------------------------------- *
 * Implementation of the AdditionFilter class : Filter subclass.
 * 
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

public class AdditionFilter extends Filter
{
    /* ------------------------------------------------------------------------- *
     * Constructor method.
     * 
     * @param nbInputs, the number of inputs of the AdditionFilter.
     * @param nbOutputs, the number of outputs of the AdditionFilter.
     * 
     * @throws, the number of inputs is not equal to the double of the 
     *          number of outputs.
     * ------------------------------------------------------------------------- */
    public AdditionFilter(int nbInputs, int nbOutputs)
    {
        // TODO: WHEN WE'LL HAVE SEEN THE THROW EXCEPTIONS
        // if(2 * nbOutputs != nbInputs)
            // Throw Exception

        super(nbInputs, nbOutputs);
    }

    /* ------------------------------------------------------------------------- *
     * Perfoms one step of computation of the AdditionFilter.
     *
     * @param input, an array containing n_I samples (one for each input,
     *               the second sequence start at input[nbInputs() / 2]).
     * 
     * @throws FilterException, inputs are incomplete.
     * 
     * @return, an array with the resulting n_O samples (one for each output).
     * ------------------------------------------------------------------------- */
    public double[] computeOneStep(double[] input)
    {
        // TODO: WHEN WE'LL HAVE SEEN THE THROW EXCEPTIONS
        // if(input.length != nbInputs())
            //Throw Exception (FilterException)

        double[] output = new double[nbOutputs()]; 

        for(int i = 0; i < nbInputs(); ++i)
            output[i] = input[i] + input[i + output.length];

        return output;
    }
}
