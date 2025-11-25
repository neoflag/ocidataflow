from pyspark.sql import SparkSession
from pyspark.sql.functions import *
import uuid

spark = (
    SparkSession.builder
        .appName("SimpleProducer")
        .getOrCreate()
)

# 5초마다 1건씩 생성되는 dummy source
input_df = (
    spark.readStream.format("rate")
        .option("rowsPerSecond", 1)
        .load()
        .withColumn("id", lit(str(uuid.uuid4())))
        .withColumn("value", concat(lit("message-"), col("value")))
        .select(col("timestamp"), col("id"), col("value"))
)

# Kafka Sink
kafka_output = (
    input_df
        .selectExpr("id as key", "to_json(struct(*)) as value")
        .writeStream
        .format("kafka")
        .option("kafka.bootstrap.servers", "localhost:9092")
        .option("topic", "test-topic")
        .option("checkpointLocation", "/tmp/checkpoints/producer")
        .outputMode("append")
        .start()
)

# Console도 출력
console_output = (
    input_df.writeStream
        .format("console")
        .option("truncate", False)
        .start()
)

spark.streams.awaitAnyTermination()