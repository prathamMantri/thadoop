package test.main;

import java.util.ArrayList;
import java.util.List;

import thadoop.s3.FileMetaData;
import thadoop.s3.S3FileSystem;

/**
 * This is a Main class 
 * Used for testing different 
 * classes
 *
 */
public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		S3FileSystem s3FileSystem=new S3FileSystem();
		s3FileSystem.getAWSConnection();
		List<FileMetaData> metaDataObjects= new ArrayList<FileMetaData>();

		metaDataObjects =s3FileSystem.getFileMetaData("testpm");
		for(int i=0; i<metaDataObjects.size();i++)
		{
			s3FileSystem.getAWSConnection();
			System.out.println(s3FileSystem.getFile(metaDataObjects.get(i)));

		}

	}

}
