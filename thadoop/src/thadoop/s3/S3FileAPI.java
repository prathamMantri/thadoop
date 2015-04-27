package thadoop.s3;

import java.io.File;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

public class S3FileAPI {
	
	private AmazonS3 s3Client;
	
	public S3FileAPI(){
		s3Client = new AmazonS3Client(new ProfileCredentialsProvider()); 
	}
	
	/*
	 * Fetches file for the given key
	 */
	public File get(String key){
		//s3Client.getObjectMetadata(arg0, arg1)
		return null;
	}
	/*
	 * Puts file to given key
	 */
	public void put(String key,File file){
		
	}
	/*
	 * Fetches all file metaData from specific url
	 */
	public List<FileMetaData> fetchMeataData(String url){
		return null;
	}
}
