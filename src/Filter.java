/* ------------------------------------------------------------------------- *
 * Implementation of the Filter class.
 * 
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

public abstract class Filter
{
    private int nbInputs, nbOutputs;

    /* ------------------------------------------------------------------------- *
     * Constructor method.
     * 
     * @param nbInputs, the number of inputs of the filter.
     * @param nbOutputs, the number of outputs of the filter.
     * ------------------------------------------------------------------------- */
    public Filter(int nbInputs, int nbOutputs)
    {
        this.nbInputs = nbInputs;
        this.nbOutputs = nbOutputs;
    }

    /* ------------------------------------------------------------------------- *
     * Getter method for nbInputs.
     * 
     * @return nbInputs, the number of inputs of the filter.
     * ------------------------------------------------------------------------- */
    public int nbInputs()
    {
        return nbInputs;
    }

    /* ------------------------------------------------------------------------- *
     * Getter method for nbOutputs.
     * 
     * @return nbOutputs, the number of outputs of the filter.
     * ------------------------------------------------------------------------- */
    public int nbOutputs()
    {
        return nbOutputs;
    }

    /* ------------------------------------------------------------------------- *
     * Perfoms one step of computation of the filter.
     *
     * @param input, an array containing n_I samples (one for each input).
     * 
     * @return, an array with the resulting n_O samples (one for each output).
     * ------------------------------------------------------------------------- */
    public abstract double[] computeOneStep(double[] input);

    /* ------------------------------------------------------------------------- *
     * Reset the filter
     * ------------------------------------------------------------------------- */
    public void reset()
    {
        // TODO: IMPLEMENT MAYBE ABSTRACT METHOD 
        return;
    }
}
