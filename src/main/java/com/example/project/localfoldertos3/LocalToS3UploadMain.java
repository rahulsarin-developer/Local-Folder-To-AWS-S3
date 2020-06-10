package com.example.project.localfoldertos3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class LocalToS3UploadMain {

	private AmazonS3 amazonS3;
	private Properties properties;
	
	LocalToS3UploadMain() {
		properties = new Properties();
		try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("application.properties")) {
			properties.load(input);
		} catch (IOException ex) {}
		
		amazonS3 = AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(
			new BasicAWSCredentials(properties.getProperty("amazon.aws.accesskey"),
			properties.getProperty("amazon.aws.secretkey"))))
			.withRegion(Regions.AP_SOUTH_1).build();
	}

	public void upload() {
		File folder = new File(properties.getProperty("local.file.path"));
		for (File file : folder.listFiles()) {
			amazonS3.putObject(properties.getProperty("amazon.aws.bucketname"), file.getName(), file);
			System.out.println(file.getName() + " -- Uploaded");
		}
	}

	public static void main(String[] args) {
		LocalToS3UploadMain s3Main = new LocalToS3UploadMain();
		s3Main.upload();
	}
}