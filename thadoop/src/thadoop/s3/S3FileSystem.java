package thadoop.s3;

import java.io.File;
import java.io.InputStream;

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
}
