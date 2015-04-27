package thadoop.message;

public enum MessageID {
	MAPPER_JOB,
	MAPPER_JOB_REPLY,//reply after completion of mappers from task tracker
	REDUCER_JOB,
	REDUCER_JOB_REPLY,//reply after completion of reducers from task tracker
	MAPREDUCE_JOB //To do Map, Reduce,Combine job
}
