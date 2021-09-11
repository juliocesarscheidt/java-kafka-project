# Some Commands

```bash
# describe a topic
kafka-topics --zookeeper localhost:2181 --describe --topic topic_0


# describe offsets from a consumer-group
kafka-consumer-groups --bootstrap-server localhost:9092 --group kafka-elasticsearch-group --describe


# return -10 offsets
kafka-consumer-groups --bootstrap-server localhost:9092 --group kafka-elasticsearch-group --reset-offsets --shift-by -10 --execute --topic topic_0

# advance 10 offsets
kafka-consumer-groups --bootstrap-server localhost:9092 --group kafka-elasticsearch-group --reset-offsets --shift-by +10 --execute --topic topic_0


# reset offset to earliest
kafka-consumer-groups --bootstrap-server localhost:9092 --group kafka-elasticsearch-group --reset-offsets --execute --to-earliest --topic topic_0

kafka-consumer-groups --bootstrap-server localhost:9092 --group kafka-elasticsearch-group --reset-offsets --execute --to-earliest --all-topics
```
