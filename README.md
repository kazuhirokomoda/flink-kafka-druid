A Flink application project using Scala and SBT.

# Issue

- When commenting out `mapper.registerModule(DefaultScalaModule)`, I encountered [Deserializing FlinkKafkaProducer's events from Kafka topic results in empty JSON records](https://stackoverflow.com/q/68734697/9728653)
- When uncommenting it, I encountered the below exception

```
 The program finished with the following exception:

com.fasterxml.jackson.databind.MappingJsonFactory@4c59e45e is not serializable. The object probably contains or references non serializable fields.
	org.apache.flink.api.java.ClosureCleaner.clean(ClosureCleaner.java:151)
	org.apache.flink.api.java.ClosureCleaner.clean(ClosureCleaner.java:126)
	org.apache.flink.api.java.ClosureCleaner.clean(ClosureCleaner.java:126)
	org.apache.flink.api.java.ClosureCleaner.clean(ClosureCleaner.java:71)
	org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.<init>(FlinkKafkaProducer.java:648)
	org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.<init>(FlinkKafkaProducer.java:602)
	org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.<init>(FlinkKafkaProducer.java:578)
	org.example.sinks.KafkaWordCountSink$.sendToKafka(KafkaWordCountSink.scala:17)
	org.example.WordCount$.main(WordCount.scala:65)
```

# Setup

## Kafka

Follow "STEP 1: GET KAFKA" to "STEP 3: CREATE A TOPIC TO STORE YOUR EVENTS" of https://kafka.apache.org/quickstart

- topic: "wordcount"
- bootstrap.servers: "localhost:9092"

## Flink session cluster

Use Flink 1.11.x and follow "Step 1: Download" and "Step 2: Start a Cluster" of https://ci.apache.org/projects/flink/flink-docs-release-1.11/try-flink/local_installation.html

## Flink job

- topic: "wordcount"
- bootstrap.servers: "localhost:9092"
- transaction.timeout.ms: "900000" (to use `FlinkKafkaProducer.Semantic.EXACTLY_ONCE`)

### Build

Install sbt https://www.scala-sbt.org/1.x/docs/index.html and run

```
sbt clean assembly
```

to get fat jar

```
target/scala-2.11/flink-kafka-druid-assembly-0.1-SNAPSHOT.jar
```

### Submit

Follow Step 3: Submit a Job of https://ci.apache.org/projects/flink/flink-docs-release-1.11/try-flink/local_installation.html

```
./bin/flink run -d -c org.example.WordCount /path/to/flink-kafka-druid-assembly-0.1-SNAPSHOT.jar
```
