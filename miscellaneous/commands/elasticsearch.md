# Some Commands

```bash
# indexes
curl -X GET 'http://localhost:9200/_cat/indices?v&pretty=true'

# create an index
curl -X PUT \
  -H 'Content-Type: application/json' \
  --url 'http://localhost:9200/twitter'

# create docs in the index
curl -X PUT \
  -H 'Content-Type: application/json' \
  --data '{
    "course": "Kafka",
    "platform": "Udemy"
  }' \
  --url 'http://localhost:9200/twitter/tweets/1'

# get index
curl -X GET \
  -H 'Content-Type: application/json' \
  --url 'http://localhost:9200/twitter/tweets/1'

# search
curl -X GET \
  -H 'Content-Type: application/json' \
  --data '{
    "query": {
      "query_string": {
        "query": "(Kafka) OR (Java)",
        "analyzer": "default",
        "fields": [
          "course",
          "platform"
        ]
      }
    }
  }' \
  --url 'http://localhost:9200/twitter/tweets/_search' | jq -r '.hits'

# remove an index
curl -X DELETE \
  -H 'Content-Type: application/json' \
  --url 'http://localhost:9200/twitter'


# repositories
curl -X GET 'http://localhost:9200/_cat/repositories?v&pretty=true'

# create a repo
curl -X PUT \
  -H 'Content-Type: application/json' \
  --data '{
    "type": "s3",
    "settings": {
      "client": "default",
      "bucket": "blackdevs-aws",
      "endpoint": "s3.sa-east-1.amazonaws.com"
    }
  }' \
  --url 'http://localhost:9200/_snapshot/s3_repository'

# remove a repo
curl -X DELETE \
  -H 'Content-Type: application/json' \
  --url 'http://localhost:9200/_snapshot/s3_repository'


# snapshots
curl -X GET 'http://localhost:9200/_snapshot/s3_repository/_all?pretty=true'

# create a snapshot on repo
curl -X PUT \
  -H 'Content-Type: application/json' \
  -d '{"indices": "twitter", "ignore_unavailable": true, "include_global_state": false}' \
  --url 'http://localhost:9200/_snapshot/s3_repository/snapshot_001?wait_for_completion=true'


# restore snapshot
curl -X POST \
  -H 'Content-Type: application/json' \
  -d '{"indices": "twitter"}' \
  --url 'http://localhost:9200/_snapshot/s3_repository/snapshot_001/_restore'

# delete snapshot
curl -X DELETE \
  -H 'Content-Type: application/json' \
  --url 'http://localhost:9200/_snapshot/s3_repository/snapshot_001'


# management
curl -X GET 'http://localhost:9200/_cat/allocation?v&pretty=true'

curl -X GET 'http://localhost:9200/_cat/health?v&pretty=true'


curl -X GET 'http://localhost:9200/_cat/master?v&pretty=true'

curl -X GET 'http://localhost:9200/_cat/nodes?v&pretty=true'


curl -X GET 'http://localhost:9200/_cat/tasks?v&pretty=true'
```
