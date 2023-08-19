#!/usr/bin/env bash

#docker cp bc642f594075:/etc/nginx .

docker run --name nginx -p 80:80 --net host -v ./nginx/html:/usr/share/nginx/html -v ./nginx/nginx.conf:/etc/nginx/nginx.conf -v ./nginx/log:/var/log/nginx -v ./nginx/conf.d/default.conf:/etc/nginx/conf.d/default.conf -d nginx