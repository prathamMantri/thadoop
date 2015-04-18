package thadoop.cluster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import thadoop.s3.FileMetaData;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class AWSClusterStatus extends ClusterStatus {


	private AmazonEC2 getAWSConnection(){
		AWSCredentials credentials = new ProfileCredentialsProvider("default").getCredentials();
		AmazonEC2 ec2 = new AmazonEC2Client(credentials);
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		ec2.setRegion(usWest2);
		System.out.println("Connected");
		return ec2;
	} 

	private List<Node> getInstances(String key, String value)
	{

		AmazonEC2 ec2= getAWSConnection();
		List<Node> node = new ArrayList<Node>();

		Iterator<Reservation> vReservations = ec2.describeInstances()
				.getReservations().iterator();
		//Step through all the reservations...
		Reservation vResItem = null;
		while (vReservations.hasNext()) {
			//For each reservation, get the instances
			vResItem = vReservations.next();
			Iterator vInstances = vResItem.getInstances().iterator();

			//For each instance, get the tags associated with it.
			while (vInstances.hasNext()) {
				Instance vInstanceItem = (Instance) vInstances.next();
				List pTags = vInstanceItem.getTags();
				Iterator vIt = pTags.iterator();

				while (vIt.hasNext()) {
					Node obj = new Node();
					Tag item = (Tag) vIt.next();
					//if the tag key macthes and the value we're looking for, we return
					if (item.getKey().equals(key) 
							&& item.getValue().equals(value)) {
						if(key.equals("Master") && value.equals("1"))
						{

							obj.setServerIP(vInstanceItem.getPublicIpAddress());
							obj.setNodeName(vInstanceItem.getInstanceId());
							node.add(obj);
							System.out.println(obj.getNodeName() + "Master");			
						}
						if(key.equals("Slave") && value.equals("1.1"))
						{
							obj.setClientIP(vInstanceItem.getPublicIpAddress());
							obj.setNodeName(vInstanceItem.getInstanceId());
							node.add(obj);
							System.out.println(obj.getNodeName() + "Slave");
						}
					}
				}
			}
		}
		return node;
	}

	public List<Node> getMaster(){
		List<Node> node = new ArrayList<Node>();
		node = getInstances("Master","1");
		return node;
	}
	public List<Node> getSlaves(){
		List<Node> node= new ArrayList<Node>();
		node = getInstances("Slave","1.1");
		return node;
	}
}
