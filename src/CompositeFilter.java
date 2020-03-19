/* ------------------------------------------------------------------------- *
 * Implementation of the CompositeFilter class.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import java.util.Vector;
import be.uliege.montefiore.oop.audio.*;

public class CompositeFilter implements Filter
{

	private int numberInputs; // number of inputs of the CompositeFilter
	private int numberbOutputs; // number of outputs of the CompositeFilter

	private Vector<Filter> filters; // Storing all the filters -- absolutely NOT sure that its the best solution


	//Constructor
	public CompositeFilter(int nbInputs, int nbOutputs)
	{
		this.numberInputs = nbInputs;
		this.numberbOutputs = nbOutputs;
		filters = new Vector<Filter>();
	}

	/**********************************************************

	Implementing methods from the Filter interface

	**********************************************************/

	public int nbInputs()
	{
		return numberInputs;
	}

	public int nbOutputs()
	{
		return numberbOutputs;
	}

	public double[] computeOneStep(double[] input)
	{
		return null;
	}

	public void reset()
	{
		return;
	}

	/**********************************************************

	Methods specific to the CompositeFilter class.

	**********************************************************/

	public void addBlock(Filter f)
	{
		filters.add(f);
	}

	public void connectBlockToBlock(Filter f1, int o1, Filter f2, int o2)
	{
		return;
	}

	public void connectBlockToOutput(Filter f1, int o1, int o2)
	{
		return;
	}

	public void connectInputToBlock(int i1, Filter f2, int i2)
	{
		return;
	}

}
