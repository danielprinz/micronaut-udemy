## MN Elastic Search

Features:
* Elastic Search Integration

### Local Elastic Search

https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html

Single node cluster with exposed port 9200.
`docker run --name mn-elastic-search -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.14.1`