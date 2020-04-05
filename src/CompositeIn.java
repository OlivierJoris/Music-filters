/* ------------------------------------------------------------------------- *
 * Implementation of the CompositeIn class which represents an input of an
 * CompositeFilter.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import be.uliege.montefiore.oop.audio.*;

public class CompositeIn implements Filter
{
	public CompositeIn(){ return;}

	public int nbInputs(){ return 1;}

	public int nbOutputs(){ return 1;}

	public double[] computeOneStep(double[] input) throws FilterException
	{
		return null;
	}

	public void reset(){ return;}

}
