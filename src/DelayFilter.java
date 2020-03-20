/* ------------------------------------------------------------------------- *
 * Implementation of the DelayFilter class.
 * 
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import be.uliege.montefiore.oop.audio.Filter;
import be.uliege.montefiore.oop.audio.FilterException;

public class DelayFilter implements Filter
{
    private int delayValue; // The delay value of the DelayFilter.
    private int count; // A counter for delaying the samples.
    private double[] delayedSamples; /* An array to memorize the actual
                                        delayed samples */

    /* ------------------------------------------------------------------------- *
     * Constructor method.
     * 
     * @param delayValue, the delay value of the DelayFilter.
     * ------------------------------------------------------------------------- */
    public DelayFilter(int delayValue)
    {   
        this.delayValue = delayValue;

        count = 0;

        delayedSamples = new double[delayValue];
        // We fill the array with zeros in order to delay the samples.
        for(int i = 0; i < delayedSamples.length; ++i)
            delayedSamples[i] = 0.0;
        
    }

    /*
    * Implementation of the nbInputs() and nbOutputs methods : the DelayFilter
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
     * Perfoms one step of computation of the DelayFilter.
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

        if(count == delayValue)
            count = 0;
        
        output[0] = delayedSamples[count];
        delayedSamples[count] = input[0];

        ++count;

        return output;
    }

    /*
    * reset() method as specified in the Filter interface. 
    */
    public void reset()
    {
        count = 0;
        // We reset the delayedSamples array.
        for(int i = 0; i < delayedSamples.length; ++i)
            delayedSamples[i] = 0.0;
    }
}
