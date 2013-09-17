package mezmer.utils

/*import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.BasicAWSCredentials
import java.io.File
import com.typesafe.config.ConfigFactory

object S3Support {
  val s3Config = ConfigFactory.load()
  val (bucket: String, awsAccessKey: String, awsSecretKey: String) = (
    s3Config.getString("s3.bucket"),
    s3Config.getString("aws.accessKey"),
    s3Config.getString("aws.secretKey")
  )

  val awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey)
  val s3client       = new AmazonS3Client(awsCredentials)

  def createBucket(bucketName: String) = {
    s3client.createBucket(bucketName)
  }

  def uploadFile(file: File) = {
    s3client.putObject(bucket, file.getName, file)
  }

}*/

// object S3Support extends S3Support