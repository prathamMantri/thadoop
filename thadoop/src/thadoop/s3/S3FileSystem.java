package thadoop.s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;



/*
 * A thin Interface to s3 to get 
 * files and put files 
 */
public class S3FileSystem {


	
	private String bucketName;


	public S3FileSystem(String bucketName){
		this.bucketName = bucketName;
	}

	private AmazonS3 getAWSConnection(){
		AWSCredentials credentials = new ProfileCredentialsProvider("default").getCredentials();
		AmazonS3 s3 = new AmazonS3Client(credentials);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		s3.setRegion(usWest2);

		return s3;
	} 

	public InputStream getFile(FileMetaData fileMetaData) throws IOException{
		AmazonS3 s3 = getAWSConnection();
		S3Object object = s3.getObject(new GetObjectRequest(fileMetaData.getFileBucket(), fileMetaData.getFileName()));
		//To get the file the with the help of file meta data
		System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
		InputStream objectData = object.getObjectContent();
		//To get the file content with the help of file object
		//displayTextInputStream(object.getObjectContent()); // Display the file content
		return objectData;
	}
	public boolean putFile(FileMetaData fileMetaData, File file){
		AmazonS3 s3 = getAWSConnection();
		try{
		s3.putObject(new PutObjectRequest(fileMetaData.getFileBucket(), "output"+file.getName(), file));
		//Logic of uploading the files into chunck.
		//We will name it as Output1#fileName
		//If there are more chunks of the file, will be named as Output#i#FileName, where i is 1....n.
		
		return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	public boolean putFile(FileMetaData fileMetaData,File file,boolean isOverWrite){
		AmazonS3 s3 = getAWSConnection();
		if(isOverWrite== true)
			s3.putObject(new PutObjectRequest(fileMetaData.getFileBucket(), "output"+file.getName(), file));
			return true;
		
	}

	public List<FileMetaData> getFileMetaData(){
		List<FileMetaData> metadata= new ArrayList<FileMetaData>();
		AmazonS3 s3= getAWSConnection();
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
		.withBucketName(bucketName)); // To gain the access of specified bucket.
		
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
		{
			FileMetaData obj = new FileMetaData();
			obj.setFileBucket(bucketName);
			obj.setFileName(objectSummary.getKey());
			obj.setFileSizeInMB(objectSummary.getSize());
			metadata.add(obj);
			System.out.println("Metadata of file is stored in object. File Name: " +objectSummary.getKey() + " and Size: "+ objectSummary.getSize()/1000000 +" MB");
		}
		return metadata;
	}

	/*
	 * This is for reading the content of file.
	 * 
	 * private static void displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null) break;

			System.out.println("  " + line);
		}
		System.out.println();
	}*/

}

