package mezmer.utils

import com.amazonaws.services.s3._
import com.amazonaws.auth.BasicAWSCredentials
import java.io.File
import com.typesafe.config.ConfigFactory

trait S3Support {
  val s3Config = ConfigFactory.load()
  val (bucket, awsAccessKey, awsSecretKey) = (
    s3Config.getString("s3.bucket"),
    System.getenv("AWS_ACCESS_KEY"),
    System.getenv("AWS_SECRET_KEY")
  )
  val awsCreds = new BasicAWSCredentials(awsAccessKey, awsSecretKey)
  val client = new AmazonS3Client(awsCreds)

  def uploadFile(filename: String, f: File) =
    client.putObject(bucket, filename, f)

  def downloadFile(filename: String) =
    client.getObject(bucket, filename)

  def listObjectsInBucket(bucketName: String) =
    client.listObjects(bucketName)

}