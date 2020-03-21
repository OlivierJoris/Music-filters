/* ------------------------------------------------------------------------- *
 * Implementation of the CompositeFilter class which is an extension of
 * the Block class.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import java.util.Vector;
import be.uliege.montefiore.oop.audio.*;

public class CompositeFilter extends Block
{

	private int numberInputs; // number of inputs of the CompositeFilter
	private int numberbOutputs; // number of outputs of the CompositeFilter

	private Vector<Block> blocks; // Storing all the filters


	//Constructor
	public CompositeFilter(int nbInputs, int nbOutputs)
	{
		this.numberInputs = nbInputs;
		this.numberbOutputs = nbOutputs;
		blocks = new Vector<Block>();
	}

	/**********************************************************

	Methods from Filter

	**********************************************************/

	public int nbInputs(){ return numberInputs;}

	public int nbOutputs(){ return numberbOutputs;}

	public double[] computeOneStep(double[] input) throws FilterException
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
		blocks.add((Block)f);
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
