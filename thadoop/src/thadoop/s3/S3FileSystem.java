package thadoop.s3;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/*
 * A thin Interface to s3 to get 
 * files and put files 
 */
public class S3FileSystem {
	public InputStream getFile(String fileName){
		return null;
	}
	public boolean putFile(File file){
		return false;
	}
	public List<FileMetaData> getFileMetaData(String uri){
		return null;
	}
	
}
