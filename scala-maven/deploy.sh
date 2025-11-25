mvn scala:run -DmainClass=HelloScalaSpark

mvn clean package

oci os object put --bucket-name seyun-dataflow-warehouse --file target/hello-scala-spark_maven-0.1.jar --force

#oci data-flow run create \
#    --compartment-id ocid1.compartment.oc1.. \
#    --application-id ocid1.dataflowapplication.oc1.ap-seoul-1 \
#    --display-name "helloscalaspark"
