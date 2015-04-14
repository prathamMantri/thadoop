package thadoop.s3;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/*
 * A thin Interface to s3 to get 
 * files and put files 
 */
public class S3FileSystem {
	public InputStream getFile(FileMetaData fileMetaData){
		return null;
	}
	public boolean putFile(FileMetaData fileMetaData,File file){
		return false;
	}
	public boolean putFile(FileMetaData fileMetaData,File file,boolean isOverWrite){
		return false;
	}
	public List<FileMetaData> getFileMetaData(String uri){
		return null;
	}
	
}
