package org.example.sinks

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema
import org.example.models.ExampleData
import com.fasterxml.jackson.databind.ObjectMapper
import java.lang
import org.apache.kafka.clients.producer.ProducerRecord
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class ExampleDataSerializationSchema(topic: String) extends KafkaSerializationSchema[ExampleData] {

  override def serialize(element: ExampleData, timestamp: java.lang.Long): ProducerRecord[Array[Byte], Array[Byte]] = {
    val elementAsBytes: Array[Byte] =
      try {
        ObjectMapperWrapper.writeValueAsBytes(element)
      } catch {
        case e: JsonProcessingException => {
          //Array[Byte]()
          null
        }
      }
    new ProducerRecord[Array[Byte], Array[Byte]](topic, elementAsBytes)
  }
}
