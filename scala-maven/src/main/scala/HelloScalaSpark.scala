/**
  * cd /Users/seyun/devpia/workspaces/dataengineer/ocidataflow/scala-maven/hellospark
  * mvn scala:run -DmainClass=HelloScalaSpark -Dspark.master=local[*]
  * mvn clean package
  * oci os object put --bucket-name seyun-dataflow-warehouse --file target/hello-scalaspark-0.1.jar
  */

import org.apache.spark.sql.SparkSession

object HelloScalaSpark {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Hello ScalaSpark Example")
      // .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // 데이터 준비
    val data = Seq(
      ("Alice", 34),
      ("Bob", 45),
      ("Cathy", 29),
      ("Seokmin", 50)
    )

    // DataFrame 생성
    val df = data.toDF("Name", "Age")

    // 출력
    df.show()

    spark.stop()
  }
}
