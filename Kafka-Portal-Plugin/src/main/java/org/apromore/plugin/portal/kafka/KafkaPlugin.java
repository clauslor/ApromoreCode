/*
 * Copyright © 2009-2017 The Apromore Initiative.
 *
 * This file is part of "Apromore".
 *
 * "Apromore" is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * "Apromore" is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package org.apromore.plugin.portal.kafka;

// Java 2 Standard Edition
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

// Java 2 Enterprise Edition
import javax.inject.Inject;

// Third party packages
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Local packages
import org.apromore.plugin.portal.DefaultPortalPlugin;
import org.apromore.plugin.portal.PortalContext;
import org.apromore.service.EventLogService;


@Component("plugin")
public class KafkaPlugin extends DefaultPortalPlugin {

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaPlugin.class.getCanonicalName());

    private String label = "Kafka";
    private String groupLabel = "Monitor";

    private Consumer<String, String> consumer;
    @Inject private EventLogService eventLogService;

    public KafkaPlugin() {
        Properties props = new Properties();
        props.put("bootstrap.servers",       "localhost:9092");
        props.put("group.id",                "test");
        props.put("enable.auto.commit",      "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer",        StringDeserializer.class);
        props.put("value.deserializer",      StringDeserializer.class);

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("my-topic"));
    }

    @Override
    public String getLabel(Locale locale) {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getGroupLabel(Locale locale) {
        return groupLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }

    @Override
    public void execute(PortalContext portalContext) {
        LOGGER.info("Execute Kafka portal plugin");

        // Configuration
        Properties props = new Properties();
        props.put("bootstrap.servers",  "localhost:9092");
        props.put("acks",               "all");
        props.put("retries",            0);
        props.put("batch.size",         16384);
        props.put("linger.ms",          1);
        //props.put("timeout.ms",         5000);
        props.put("max.block.ms",       5000);
        //props.put("metadata.fetch.timeout.ms", 5000);
        //props.put("request.timeout.ms", 5000);
        props.put("buffer.memory",      33554432);
        props.put("key.serializer",     StringSerializer.class);
        props.put("value.serializer",   StringSerializer.class);

        // Send a key-value pair to a Kafka topic
        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            // TODO: create my-topic if it doesn't exist
            producer.send(new ProducerRecord<String, String>("my-topic", "key", "value"));
        }

        LOGGER.info("Executed Kafka portal plugin");
    }
}
