
spark_application = loadadw_simplified.py # replace with ```loadadw_with_wallet_objectstorage.py``` if required

oci os object put --bucket-name <bucket> --file ${spark_application}

oci data-flow application create \
    --compartment-id <compartment_ocid> \
    --display-name "PySpark Load ADW Simplified" \
    --driver-shape VM.Standard2.1 \
    --executor-shape VM.Standard2.1 \
    --num-executors 1 \
    --spark-version 3.0.2 \
    --file-uri oci://<bucket>@<namespace>/${spark_application} \
    --language Python
    --configuration "spark.oracle.datasource.enabled=true"

oci data-flow run create \
    --application-id <application_ocid> \
    --compartment-id <compartment_ocid> \
    --display-name 'PySpark Load ADW Simplified"