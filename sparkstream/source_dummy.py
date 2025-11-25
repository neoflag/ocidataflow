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

# Console도 출력
console_output = (
    input_df.writeStream
        .format("console")
        .option("truncate", False)
        .start()
)

# spark stream으로 대기 하기 위해서는 아래 코드가 필요
# 두지 않으면 1회만 실행하고 바로 종료됨
spark.streams.awaitAnyTermination()

# spark stream 시간 제한 두는 방법 예시
# query.awaitTermination(3600)  # 1시간 후 자동 종료