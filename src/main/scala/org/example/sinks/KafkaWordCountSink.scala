package org.example.sinks

import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.example.models.ExampleData
import java.util.Properties

object KafkaWordCountSink {
  def sendToKafka(exampleDataStream: DataStream[ExampleData]): DataStreamSink[ExampleData] = {
    val topic = "wordcount"
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("transaction.timeout.ms", "900000")

    val producer = new FlinkKafkaProducer[ExampleData](
      topic,
      new ExampleDataSerializationSchema(topic),
      properties,
      FlinkKafkaProducer.Semantic.EXACTLY_ONCE
    )

    exampleDataStream.addSink(producer)
  }
}
