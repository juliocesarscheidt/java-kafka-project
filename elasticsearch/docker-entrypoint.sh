#!/bin/sh

set -ex

echo "${AWS_ACCESS_KEY_ID}" | bin/elasticsearch-keystore add --stdin --force s3.client.default.access_key
echo "${AWS_SECRET_ACCESS_KEY}" | bin/elasticsearch-keystore add --stdin --force s3.client.default.secret_key

/tini -s -- /usr/local/bin/docker-entrypoint.sh "$@"
