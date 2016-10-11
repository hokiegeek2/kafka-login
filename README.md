# Kafka-Login

The kafka-login project is designed to provide Authorizers for Kafka 0.9.x where authentication is via a Kerberos alternative. The initial implementation is SSL.

# Server, Producer, and Consumer CLI Instructions

## Server
bin/kafka-server-start.sh config/secured-server.properties

## Producer
bin/kafka-console-producer.sh --broker-list localhost:9093 --topic secured-topic --producer.config/producer.secured.properties 

## Consumer
bin/kafka-console-consumer.sh --consumer.config config/consumer-secured.properties --zookeeper localhost:2181 ---topic
secured-topic --from-beginning --new-consumer --bootstrap-server localhost:9093
 _ Important: the --new-consumer and --bootstrap-server arguments are required. _ Otherwise, the pre-0.9.x client is
used, which does not support the ACL feature added for the 0.9.0.0 release.