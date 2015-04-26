/**
 * 
 */
/**
 * @author Krishnaja
 *
 */
package thadoop.mapreduce;

import java.io.Serializable;

public class MapReduce implements Serializable
{
	private static final long serialVersionUID = -6867043043090117875L;
	public String mapper(){
		return "This is Mapper Method";
	}
	public void reducer(){
		
		
	}
	public void combiner(){
		
	}
}