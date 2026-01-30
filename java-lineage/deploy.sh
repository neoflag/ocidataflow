# vscode run 실행 가능

mvn clean package

oci os object put --bucket-name seyun-dataflow-warehouse --file target/lineage-java-spark-0.1.jar --force

oci data-flow run create \
   --compartment-id ocid1.compartment.oc1..aaaaaaaa2xmgxewcwbfy7qbt2gj7sfakwm7f2rm5lkjpbwcfsj4kvw3x23iq \
   --application-id ocid1.dataflowapplication.oc1.ap-seoul-1.anuwgljrvsea7yiagyxgqii6cpgr3glwn6on7jgxvnpobuvv3dljjjpza34a \
   --display-name "lineagespark-java"
