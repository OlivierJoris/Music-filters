import be.uliege.montefiore.oop.audio.Filter;

/**
 * Interface of a Block which is an extension of the Filter interface.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public interface BlockInterface extends Filter
{
	/**
	 * Returns the main filter of the Block.
	 *
	 * @return The main Filter of the Block.
	*/
	public Filter getMainFilter();

	/**
	 * Returns the Block connected to the input inputNumber.
	 *
	 * @param inputNumber The index of the considered input.
	 *
	 * @throws IndexOutOfBoundsException inputNumber is out of bounds.
	 *
	 * @return The Block linked to the input inputNumber.
	*/
	Block getInput(int inputNumber) throws IndexOutOfBoundsException;

	/**
	 * Returns the outputNumber-th output of the Block.
	 *
	 * @param outputNumber The index of the considered output.
	 * @param index The index inside the output outputNumber.
	 *
	 * @throws IndexOutOfBoundsException outputNumber is out of bounds.
	 * @throws IndexOutOfBoundsException index is out of bounds.
	 *
	 * @return The Block linked to the output outputNumber index.
	*/
	Block getOutput(int outputNumber, int index) throws IndexOutOfBoundsException;

	/**
	 * Gets the availability status of the input inputNumber.
	 *
	 * @param inputNumber The index of the considered input.
	 *
	 * @throws IndexOutOfBoundsException inputNumber is out of bounds.
	 *
	 * @return True if the input inputNumber is available. Else, false.
	*/
	boolean getInputAvailability(int inputNumber) throws IndexOutOfBoundsException;

	/**
	 * Sets the input inputNumber as f.
	 *
	 * @param f The Block to which we want to make a connection.
	 * @param inputNumber The index of the considered input.
	 *
	 * @throws NullPointerException f is null.
	 * @throws IndexOutOfBoundsException inputNumber is out of bounds.
	*/
	void setInput(Block f, int inputNumber) throws NullPointerException, IndexOutOfBoundsException;

	/**
	 * Sets the output outputNumber as f.
	 *
	 * @param f The Block to which we want to make a connection.
	 * @param outputNumber The index of the considered output.
	 *
	 * @throws NullPointerException f is null.
	 * @throws IndexOutOfBoundsException outputNumber is out of bounds.
	*/
	void setOutput(Block f, int outputNumber) throws NullPointerException, IndexOutOfBoundsException;

	/**
	 * Sets the input inputNumber availability status as status.
	 *
	 * @param inputNumber The index of the considered input.
	 * @param status The new status.
	 *
	 * @throws IndexOutOfBoundsException inputNumber is out of bounds.
	*/
	void setInputAvailability(int inputNumber, boolean status) throws IndexOutOfBoundsException;
}
