"""
deploy

oci os object put --bucket-name seyun-dataflow-warehouse --file hellospark.py

oci data-flow run create \
    --application-id ocid1.dataflowapplication.oc1.ap-seoul-1.anuwgljrvsea7yiaejsv7enm7pj44vvxan6y4gx5ynyn2pt5hxjs7q6vg34q \
    --compartment-id ocid1.compartment.oc1..aaaaaaaa2xmgxewcwbfy7qbt2gj7sfakwm7f2rm5lkjpbwcfsj4kvw3x23iq \
    --display-name 'HelloSpark Launch !!' 

"""

import os, time
import pyspark
from pyspark.sql import SparkSession

java_home = os.getenv('JAVA_HOME')
if java_home is None:
    print("JAVA_HOME is not set. Please set JAVA_HOME environment variable.")
else:
    print(f"JAVA_HOME is set to: {java_home}")

spark_home = os.getenv('SPARK_HOME')
if spark_home is None:
    print("SPARK_HOME is not set. Please set SPARK_HOME environment variable.")
else:
    print(f"SPARK_HOME is set to: {spark_home}")

try:
    spark = SparkSession.builder.appName("Hello Spark").getOrCreate()

    data = [("Alice", 34), ("Bob", 45), ("Cathy", 29), ("Seokmin", 50)]
    columns = ["Name", "Age"]
    df = spark.createDataFrame(data, columns)

    df.show()
except Exception as e:
    print(f"An error occurred: {e}")
finally:
    print("Spark job completed successfully. Paused.......")
    #ui 확인을 위해 대기
    time.sleep(300)
    spark.stop()