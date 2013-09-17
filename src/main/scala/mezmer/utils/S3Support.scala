package mezmer.utils

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.BasicAWSCredentials
import java.io.File
import com.typesafe.config.ConfigFactory
import scala.util.Try

trait S3Support {
  val s3Config = ConfigFactory.load()
  lazy val (bucket, awsAccessKey, awsSecretKey) = (
    Try(s3Config.getString("s3.bucket")).getOrElse(System.getenv("S3_BUCKET")),
    Try(s3Config.getString("aws.accessKey")).getOrElse(System.getenv("AWS_ACCESS_KEY")),
    Try(s3Config.getString("aws.secretKey")).getOrElse(System.getenv("AWS_SECRET_KEY"))
  )

  val awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey)
  val s3client       = new AmazonS3Client(awsCredentials)

  def createBucket(bucketName: String) = {
    s3client.createBucket(bucketName)
  }

  def uploadFile(file: File) = {
    s3client.putObject(bucket, file.getName, file)
  }

}