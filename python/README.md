Prerequisites
    1. Visual Studio Code Installed
    2. Python Installed (3.11 +)
    3. Java Installed (17 +)

spark 설치
https://archive.apache.org/dist/spark/
사용할 버전과 일치하는 버전 권장 (각 버전에 따라 dependency가 상이)
python, java 또한 data flow 버전 확인 후 해당 버전 일치하도록 설치 권장


hadoop 라이브러리 포함 빌드 결과물 받는 것을 추천
spark-3.5.0-bin-hadoop3-scala2.13.tgz    


환경 변수 선언
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export SPARK_HOME=/Users/devpia/apache/spark/spark-3.5.0

pyspark 설치
$ pip install pyspark==3.5.0