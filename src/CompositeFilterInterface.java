import be.uliege.montefiore.oop.audio.*;

/**
 * Interface of a CompositeFilter which is an extension of the Filter interface.
 *
 * @author Maxime GOFFART (180521) and Olivier JORIS (182113).
*/
public interface CompositeFilterInterface extends Filter
{
	/**
	 * Adds a Filter to the CompositeFilter.
	 *
	 * @param f The filter we want to add.
	 *
	 * @throws NullPointerException Filter f is null.
	*/
	void addBlock(Filter f) throws NullPointerException;

	/**
	 * Connects the output o1 of the Filter f1 to the input i2 of the Filter f2.
	 *
	 * @param f1 One of the Filter we wish to connect.
	 * @param o1 The output number of the Filter f1 we want to conenct.
	 * @param f2 The other Filter we wish to connect.
	 * @param i2 The input number of the Filter f2 we want to connect.
	 *
	 * @throws Exception An error occured.
	*/
	void connectBlockToBlock(Filter f1, int o1, Filter f2, int i2) throws Exception;

	/**
	 * Connects the output o1 of the Filter f1 to the output o2 of the CompositeFilter.
	 *
	 * @param f1 The Filter we want to connect to the output of the CompositeFilter.
	 * @param o1 The output number of the Filter f1 we want to connect.
	 * @param o2 The output number of the CompositeFilter we want to connect.
	 *
	 * @throws Exception An error occured.
	*/
	void connectBlockToOutput(Filter f1, int o1, int o2) throws Exception;

	/**
	 * Connects the input i1 of the CompositeFilter to the input i2 of the Filter f2.
	 *
	 * @param i1 The input number of the CompositeFilter we want to connect.
	 * @param f2 The Filter we want to connect to the input of the CompositeFilter.
	 * @param i2 The input number of the Filter f2 we want to connect.
	 *
	 * @throws Exception An error occured.
	*/
	void connectInputToBlock(int i1, Filter f2, int i2) throws Exception;
}
