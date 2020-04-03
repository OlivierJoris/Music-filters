/* ------------------------------------------------------------------------- *
 * Interface of a CompositeFilter which is an extension of the Block interface.
 *
 * @authors Maxime GOFFART (180521) and Olivier JORIS (182113).
 * ------------------------------------------------------------------------- */

import be.uliege.montefiore.oop.audio.Filter;

public interface CompositeFilterInterface extends Filter
{
	// Adding a new block f to the composite filter
	void addBlock(Filter f);

	// Connecting output o1 of the block f1 to the input i2 of the block f2.
	void connectBlockToBlock(Filter f1, int o1, Filter f2, int i2);

	// Connecting the output o1 of the block f1 to the output o2 of the composite filter.
	void connectBlockToOutput(Filter f1, int o1, int o2);

	// Connecting the input i1 of the composite filter to the input i2 of the block f2.
	void connectInputToBlock(int i1, Filter f2, int i2);
}
