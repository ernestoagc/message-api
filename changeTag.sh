#!/bin/bash
sed "s/tagVersion/$1/g" deployment.yaml > deployment-api.yaml