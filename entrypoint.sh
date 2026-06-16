#!/bin/sh
envsubst < /etc/grafana/provisioning/datasources/datasources.yml \
  > /tmp/datasources_resolved.yml
mv /tmp/datasources_resolved.yml \
   /etc/grafana/provisioning/datasources/datasources.yml
exec /run.sh