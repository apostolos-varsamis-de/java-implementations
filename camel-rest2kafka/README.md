# USAGE

Preliminaries:
Kafka is running. In this example three kafka-nodes and three zookeeper nodes are running
**Kafka-nodes:**

- kafka1.192.168.31.41:9092
- kafka2.192.168.31.42:9092
- kafka3.192.168.31.43:9092
**Zookeeper-nodes**
- kafka1.192.168.31.41:2181
- kafka2.192.168.31.42:2181
- kafka3.192.168.31.43:2181
2. Run Application:
   ```
   java -jar camel-rest2kafka-1.0-SNAPSHOT.jar
   ```
3. Send POST request with a JSON body to the endpoint defined in component's configuration
    `http://localhost:9001/context/stream`

```bash
   curl -X POST \
     http://localhost:9001/context/stream \
     -H 'Cache-Control: no-cache' \
     -H 'Content-Type: application/json' \
     -d '{"data":"Information"}'
```

### Create a topic 
in einem der Kafka-nodes:
```bash
    /opt/kafka/bin/kafka-topics.sh \
    --zookeeper kafka1.192.168.31.41:2181 \
    --create \
    --if-not-exists \
    --topic test \
    --replication-factor 1 \
    --partitions 3
```
### Delete a topic 
in einem der Kafka-nodes:
```bash
    /opt/kafka/bin/kafka-topics.sh \
    --zookeeper kafka1.192.168.31.41:2181 \
    --delete \
    --if-exists \
    --topic test
```

### Show contents of the topic:
```bash
    /opt/kafka/bin/kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 
    --topic test 
    --from-beginning
```