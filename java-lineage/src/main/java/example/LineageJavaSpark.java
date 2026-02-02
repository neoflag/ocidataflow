package example;

import java.util.*;

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
public class LineageJavaSpark {
        // Customize these before you start.
        // private static String inputPath = "oci://<bucket>@<tenancy>/fake_data.csv";
        // private static String outputPath =
        // "oci://<bucket>@<tenancy>/fake_data.parquet";

        public static void main(String[] args) throws Exception {
                // Get our Spark session.
                SparkSession spark = SparkSession.builder()
                                .appName("OracleDB1_to_OracleDB2_Copy")
                                // .master("local[*]") // 로컬 실행, 클러스터 환경에서는 제거
                                .getOrCreate();

                // printDf(spark);
                // copyTable(spark);
                //copyCSV(spark);
                copyCSV2Table(spark);

                // Dataset<Row> df = spark.read().csv(inputPath);
                // df.write().mode(SaveMode.Overwrite).format("parquet").save(outputPath);
                System.out.println("HelloSpark complete.");

                try {
                        Thread.sleep(1000); // 10초 대기
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }

                spark.stop();
        }

        /**
         * CSV 파일을 읽어 Parquet로 저장
         * 이 내용은 Lineage 지원
         * 
         * @param spark
         */
        private static void copyCSV2Table(SparkSession spark) {
                // Object Storage CSV 경로
                String inputCsvPath = "oci://seyun-lineage-bucket@apackrsct01/input/sample.csv";

                // CSV 읽기
                Dataset<Row> df = spark.read()
                                .option("header", "true") // CSV 헤더 있음
                                .option("inferSchema", "true") // 스키마 자동 추론
                                .csv(inputCsvPath);

                // 콘솔 출력
                df.show(20, false);

                String url = "jdbc:oracle:thin:@(description=(retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1521)(host=adb.ap-seoul-1.oraclecloud.com))(connect_data=(service_name=yh0olybn5pqce4n_seyunatp19c_low.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))";
                Properties props = new Properties();
                props.put("user", "admin");
                props.put("password", "RHKS12flwk#_");
                props.put("driver", "oracle.jdbc.OracleDriver");
                String tgtTable = "SAMPLE_EMPLOYEE";

                df.write().mode("append") // overwrite / append
                          .jdbc(url, tgtTable, props);
        }

        /**
         * CSV 파일을 읽어 Parquet로 저장
         * 이 내용은 Lineage 지원
         * 
         * @param spark
         */
        private static void copyCSV(SparkSession spark) {
                // Object Storage CSV 경로
                String inputCsvPath = "oci://seyun-lineage-bucket@apackrsct01/input/sample.csv";

                // Object Storage Parquet 출력 경로
                String outputParquetPath = "oci://seyun-lineage-bucket@apackrsct01/output/sample_parquet";

                // CSV 읽기
                Dataset<Row> df = spark.read()
                                .format("csv")
                                .option("header", "true") // CSV 헤더 있음
                                .option("inferSchema", "true") // 스키마 자동 추론
                                .load(inputCsvPath);

                // 콘솔 출력
                df.show(20, false);

                // Parquet로 저장
                df.write()
                                .mode("overwrite")
                                .parquet(outputParquetPath);
        }

        private static void copyTable(SparkSession spark) {
                String url = "jdbc:oracle:thin:@(description=(retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1521)(host=adb.ap-seoul-1.oraclecloud.com))(connect_data=(service_name=yh0olybn5pqce4n_seyunatp19c_low.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))";
                /*
                 * select 예제
                 * // 출력
                 * df0.show();
                 * 
                 * // SQL 쿼리를 서브쿼리 형태로 사용
                 * String query =
                 * "(SELECT CUST_ID, CUST_FIRST_NAME, CUST_LAST_NAME, CUST_YEAR_OF_BIRTH, CUST_MARITAL_STATUS FROM TCUSTOMERS0 ORDER BY CUST_ID, CUST_FIRST_NAME, CUST_LAST_NAME) TMP"
                 * ;
                 * 
                 * Properties props = new Properties();
                 * props.put("user", "admin");
                 * props.put("password", "RHKS12flwk#_");
                 * props.put("driver", "oracle.jdbc.OracleDriver");
                 * 
                 * Dataset<Row> df = spark.read().jdbc(url, query, props);
                 * df.show();
                 */

                // https://blogs.oracle.com/cloud-infrastructure/new-data-lineage-features-oci-data-catalog
                // https://sales-emea-japac.slack.com/archives/CTX1474TW/p1719944328105519
                // https://docs.oracle.com/en-us/iaas/Content/data-catalog/using/view-data-lineage-dis.htm#data-lineage-data-flow
                // ===== Oracle DB1 (Source) =====
                // String srcUrl = "jdbc:oracle:thin:@//db1-host:1521/DB1PDB";
                String srcTable = "TCUSTOMERS0";

                Properties props = new Properties();
                props.put("user", "admin");
                props.put("password", "RHKS12flwk#_");
                props.put("driver", "oracle.jdbc.OracleDriver");

                // ===== Oracle DB2 (Target) =====
                // String tgtUrl = "jdbc:oracle:thin:@//db2-host:1521/DB2PDB";
                String tgtTable = "TCUSTOMERS1";

                // ===== Read from Oracle DB1 =====
                Dataset<Row> df = spark.read()
                                .jdbc(url, srcTable, props);

                // (선택) 데이터 확인
                df.printSchema();
                df.show(5);

                // ===== Write to Oracle DB2 =====
                df.write()
                                .mode("append") // overwrite / append
                                .jdbc(url, tgtTable, props);
        }

        private static void printDf(SparkSession spark) {
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
                Dataset<Row> df0 = spark.createDataFrame(data, schema);
        }

}
