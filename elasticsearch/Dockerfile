FROM docker.elastic.co/elasticsearch/elasticsearch:7.7.0

WORKDIR /usr/share/elasticsearch

# install repository plugins
RUN bin/elasticsearch-plugin \
    install repository-s3 repository-hdfs --silent --batch

COPY docker-entrypoint.sh /opt/docker-entrypoint.sh

ENTRYPOINT ["/opt/docker-entrypoint.sh"]
CMD ["eswrapper"]
