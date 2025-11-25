package example;

import java.sql.Time;
import java.util.*;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.*;


/*
 * This example shows converting CSV to Parquet in Spark.
 * 
 * Before you begin, upload fake_data.csv from the sample bundle and customize the paths.
 * 
 * In OCI CLI run:
 * oci os object put --bucket-name <bucket> --file fake_data.csv
 *
 */

public class HelloJavaSpark {
        // Customize these before you start.
        private static String inputPath = "oci://<bucket>@<tenancy>/fake_data.csv";
        private static String outputPath = "oci://<bucket>@<tenancy>/fake_data.parquet";

        public static void main(String[] args) throws Exception {
                // Get our Spark session.
                SparkSession spark = SparkSession.builder()
                                .appName("CreateDataFrameExample")
                                .master("local[*]") // 로컬 실행
                                .getOrCreate();

                // 데이터 준비
                List<Row> data = Arrays.asList(
                                RowFactory.create("Alice", 34),
                                RowFactory.create("Bob", 45),
                                RowFactory.create("Cathy", 29),
                                RowFactory.create("Seokmin", 50));

                // 스키마 정의
                StructType schema = DataTypes.createStructType(new StructField[] {
                                DataTypes.createStructField("Name", DataTypes.StringType, false),
                                DataTypes.createStructField("Age", DataTypes.IntegerType, false)
                });

                // DataFrame 생성
                Dataset<Row> df = spark.createDataFrame(data, schema);

                // 출력
                df.show();

                // Dataset<Row> df = spark.read().csv(inputPath);
                // df.write().mode(SaveMode.Overwrite).format("parquet").save(outputPath);
                System.out.println("HelloSpark complete.");

                try{
                    Thread.sleep(10000); // 10초 대기
                } catch(InterruptedException e){
                    e.printStackTrace();
                }

                spark.stop();
        }
}