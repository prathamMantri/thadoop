package thadoop.s3;

import java.io.File;
import java.io.InputStream;
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
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;



/*
 * A thin Interface to s3 to get 
 * files and put files 
 */
public class S3FileSystem {


	AmazonS3 s3 = null;
	String bucketName="testpm";
	ObjectListing objectListing;
	AWSCredentials credentials = null;

	public void getAWSConnection()
	{
		try {
			credentials = new ProfileCredentialsProvider("default").getCredentials();
			AmazonS3 s3 = new AmazonS3Client(credentials);

			Region usWest2 = Region.getRegion(Regions.US_WEST_2);
			s3.setRegion(usWest2);
			objectListing = s3.listObjects(new ListObjectsRequest()
			.withBucketName(bucketName));
		}
		catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		catch (Exception e) {
		throw new AmazonClientException(
				"Cannot load the credentials from the credential profiles file. " +
						"Please make sure that your credentials file is at the correct " +
						"location (C:\\Users\\Prathamesh\\.aws\\credentials), and is in valid format.",
						e);
	}
	}

	public InputStream getFile(FileMetaData fileMetaData){
		s3 = new AmazonS3Client(credentials);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		s3.setRegion(usWest2);
		S3Object object = s3.getObject(new GetObjectRequest(fileMetaData.getFileBucket(), fileMetaData.getFileName()));
		System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
		InputStream objectData = object.getObjectContent();
		//displayTextInputStream(object.getObjectContent());
		return objectData;
	}
	public boolean putFile(FileMetaData fileMetaData,File file){
		return false;
	}
	public boolean putFile(FileMetaData fileMetaData,File file,boolean isOverWrite){
		return false;
	}

	public List<FileMetaData> getFileMetaData(String bucketName){
		List<FileMetaData> metadata= new ArrayList<FileMetaData>();

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
}

