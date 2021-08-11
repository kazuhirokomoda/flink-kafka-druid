package org.example

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.flink.streaming.api.scala._
import org.example.models.ExampleData
import org.example.sinks.KafkaWordCountSink

/**
 * Implements the "WordCount" program that computes a simple word occurrence histogram
 * over some sample data
 *
 * This example shows how to:
 *
 *   - write a simple Flink program.
 *   - use Tuple data types.
 *   - write and use user-defined functions.
 */
object WordCount {
  val WORDS = Array[String](
    "To be, or not to be,--that is the question:--",
    "Whether 'tis nobler in the mind to suffer",
    "The slings and arrows of outrageous fortune",
    "Or to take arms against a sea of troubles,"
  )

  def main(args: Array[String]) {

    // set up the stream execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // get input data
    val text: DataStream[String] = env.fromElements(WORDS: _*)

    val counts: DataStream[(String, Int)] = text
      // split up the lines in pairs (2-tuples) containing: (word, 1)
      .flatMap(_.toLowerCase.split("\\W+"))
      .filter(_.nonEmpty)
      .map((_, 1))
      // group by the tuple field "0" and sum up tuple field "1"
      .keyBy(_._1)
      .sum(1)

    val stream: DataStream[ExampleData] = counts.map { x =>
      ExampleData(x._1, x._2, x._1.charAt(0), System.currentTimeMillis)
    }

    // send to Kafka
    KafkaWordCountSink.sendToKafka(stream)
      .uid("kafka-wordcount-sink-id")
      .name("kafka-wordcount-sink-name")

    env.execute("Scala (Stream) WordCount Example")
  }
}

