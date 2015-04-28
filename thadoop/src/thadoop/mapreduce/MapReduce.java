package thadoop.mapreduce;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * contains word count program 
 */
public class MapReduce implements Serializable
{
	private static final long serialVersionUID = -6867043043090117875L;
	private Path inputPath;
	private Path outputPath;
	
	public MapReduce(Path inputPath,Path outputPath){
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}
	
	public Map<String,Integer> mapper(Map<Integer,String> input){
		Map<String,Integer> output = new HashMap<String,Integer>();
		for(Integer lineNum: input.keySet()){
			String line = input.get(lineNum);
			for(String word: line.split("\\s+")){
				Integer val = output.get(word);
				if(val == null)
					val = 0;
				val = val + 1;
				output.put(word, val);
			}
		}
		return output;
	}
	public Map<String,Integer> reducer(Map<String,List<Integer>> input){
		Map<String,Integer> output = new HashMap<String,Integer>();
		for(String word: input.keySet()){
			Integer sum = 0;
			for(Integer val : input.get(word)){
				sum = sum + val;
			}
			output.put(word, sum);
		}
		return output;
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