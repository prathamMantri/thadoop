package thadoop.message;
import thadoop.mapreduce.MapReduce;

/*
 * This class is used to send MessageID, MapReduceCombine Job to Task tracker
 */

public class MapReduceMessage extends Message{
	
	private static final long serialVersionUID = -6867043043090117875L;
	private MapReduce mapreduce;
	
	public MapReduceMessage (MessageID messageID, MapReduce mapreduce) {
	        this.messageID = messageID;
	        this.mapreduce = mapreduce;
	    }
	
	    public MessageID getId() { 
	        return messageID;
	    }
	    
	    public void setId(MessageID messageID) {
	        this.messageID = messageID;
	    }
	    
	    public MapReduce getMapreduce() {
			return mapreduce;
		}

		public void setMapreduce(MapReduce mapreduce) {
			this.mapreduce = mapreduce;
		}
	}

