"""
csvtest.py
""" 
from pyspark.sql import SparkSession  

spark = SparkSession.builder.appName("Airlines").getOrCreate()  

# airlines.csv를 읽어 DataFrame으로 만들기
airlines = spark.read.format("csv").option("header", "true").option("inferSchema","true").load("oci://seyun-dataflow-warehouse@apackrsct01/sqlendpoint-externaldata/csvtest.csv")  

# managed table로 저장
airlines.write.format("parquet").mode('overwrite').saveAsTable("csvtest_managed")  

# 데이터를 한 줄 추가한 DataFrame(airlines_ex)을 생성하고, external table로 저장
# -> 옵션으로 저장 경로를 지정하면 external table 생성
df = spark.createDataFrame([("TD", "Test Data")]) 
airlines_ex = airlines.union(df) 
airlines_ex.write.format("parquet").mode('overwrite').option('path', "oci://seyun-dataflow-warehouse@apackrsct01/meta-external-table").saveAsTable("csvtest_external")  

spark.stop()