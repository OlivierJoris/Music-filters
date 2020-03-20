/* ------------------------------------------------------------------------- *
 * Implementation of the GainFilter class.
 * 
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import be.uliege.montefiore.oop.audio.Filter;
import be.uliege.montefiore.oop.audio.FilterException;

public class GainFilter implements Filter
{
    private double gainValue; // The gain value of the GainFilter.

    /* ------------------------------------------------------------------------- *
     * Constructor method.
     * 
     * @param gainValue, the gain value of the GainFilter.
     * ------------------------------------------------------------------------- */
    public GainFilter(double gainValue)
    {   
        this.gainValue = gainValue;
    }

    /*
    * Implementation of the nbInputs() and nbOutputs() methods : the GainFilter
    only has 1 input and 1 output.
    */
   public int nbInputs()
   {
      return 1;
   }
   
   public int nbOutputs()
   {
      return 1;
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
    public double[] computeOneStep(double[] input) throws FilterException
    {
        if(input.length != nbInputs())
            throw new FilterException("Invalid number of inputs");

        double[] output = new double[1]; 

        output[0] = input[0] * gainValue;

        return output;
    }

    /*
    * reset() method as specified in the Filter interface. 
    */
    public void reset()
    {
        // We do not need to reset anything.
        return;
    }
}
