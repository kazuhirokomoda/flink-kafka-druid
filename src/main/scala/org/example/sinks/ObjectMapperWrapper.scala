package org.example.sinks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object ObjectMapperWrapper extends java.io.Serializable {
  // this lazy is the important trick here
  // @transient adds some safety in current Scala (see also Update section)
  // https://stackoverflow.com/a/48385665
  @transient lazy val mapper = {
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper
  }

  //def readValue[T](content: String, valueType: Class[T]): T = mapper.readValue(content, valueType)
  def writeValueAsBytes(value: Object): Array[Byte] = mapper.writeValueAsBytes(value)
}
