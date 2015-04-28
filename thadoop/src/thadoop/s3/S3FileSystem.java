package thadoop.s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
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
		System.out.println("Connected");
		return s3;
	} 

	public InputStream getFile(FileMetaData fileMetaData) throws IOException{
		AmazonS3 s3 = getAWSConnection();
		//for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
			
		S3Object object = s3.getObject(new GetObjectRequest(this.bucketName, fileMetaData.getFileName()));
		InputStream objectData = object.getObjectContent();
		System.out.println(IOUtils.toString(objectData));
		
		return objectData;
	}
	
	public boolean putFile(FileMetaData fileMetaData, File file) throws Exception{
		AmazonS3 s3 = getAWSConnection();
		s3.putObject(new PutObjectRequest(this.bucketName, fileMetaData.getFileName(), file));
		return true;
	}
	
	/*public boolean putFile(FileMetaData fileMetaData,File file,boolean isOverWrite){
		AmazonS3 s3 = getAWSConnection();
		if(isOverWrite== true)
			s3.putObject(new PutObjectRequest(this.bucketName, fileMetaData.getFileName(), file));
			return true;
	}*/

	public List<FileMetaData> getFileMetaData(String path) throws AmazonServiceException, AmazonClientException{
		List<FileMetaData> metadata= new ArrayList<FileMetaData>();
		
		AmazonS3 s3= getAWSConnection();	
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
		.withBucketName(bucketName).withDelimiter("/").withPrefix(path)); // To gain the access of specified bucket.
		
		 objectListing.getObjectSummaries().remove(0);
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
		{
			FileMetaData obj = new FileMetaData();
			obj.setFileName(objectSummary.getKey());
			obj.setFileSizeInKB(objectSummary.getSize()/1000);
			metadata.add(obj);
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

