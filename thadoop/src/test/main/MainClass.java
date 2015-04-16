package test.main;

import java.io.IOException;
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

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		S3FileSystem s3FileSystem=new S3FileSystem("testpm");
		
		List<FileMetaData> metaDataObjects= new ArrayList<FileMetaData>();
		
		metaDataObjects =s3FileSystem.getFileMetaData();
		for(int i=0; i<metaDataObjects.size();i++)
		{
			System.out.println(s3FileSystem.getFile(metaDataObjects.get(i)));
}

	}

}
