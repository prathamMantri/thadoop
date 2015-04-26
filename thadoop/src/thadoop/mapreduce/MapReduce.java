/**
 * 
 */
/**
 * @author Krishnaja
 *
 */
package thadoop.mapreduce;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import thadoop.s3.FileMetaData;

public class MapReduce implements Serializable
{
	private static final long serialVersionUID = -6867043043090117875L;
	private Path inputPath;
	private Path outputPath;
	
	public MapReduce(Path inputPath,Path outputPath){
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}
	
	public Map<String,Integer> mapper(FileMetaData fileMetaData){
		return null;
	}
	public Map<String,Integer> reducer(List<FileMetaData> fileMetaDatas){
		return null;
	}
	public Path getInputPath() {
		return inputPath;
	}

	public void setInputPath(Path inputPath) {
		this.inputPath = inputPath;
	}

	public Path getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(Path outputPath) {
		this.outputPath = outputPath;
	}
	
}