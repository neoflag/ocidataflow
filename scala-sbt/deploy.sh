sbt run

sbt clean assembly

mv target/scala-2.12/scala2_12_20-assembly-0.1.jar target/scala-2.12/hello-scala-spark_sbt-0.1.jar

oci os object put --bucket-name seyun-dataflow-warehouse --file target/scala-2.12/hello-scala-spark_sbt-0.1.jar --force

#oci data-flow run create \
#    --compartment-id ocid1.compartment.oc1.. \
#    --application-id ocid1.dataflowapplication.oc1.ap-seoul-1 \
#    --display-name "helloscalaspark"
