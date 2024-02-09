#!/bin/bash

# CA creates private key and root CA certificate
openssl genrsa -out root.key
openssl req -new -x509 -key root.key -out root.crt  -subj "/CN=localhost" -nodes

# keystore
keytool -keystore grpc.keystore.jks -storepass changeit -alias localhost -validity 3650 -genkey -keyalg RSA -dname "CN=localhost"

# create CSR (certificate signing request)
keytool -keystore grpc.keystore.jks -storepass changeit -alias localhost -certreq -file grpc-signing-request.crt

# CA signs the certificate
openssl x509 -req -CA root.crt -CAkey root.key -in grpc-signing-request.crt -out grpc-signed.crt -days 3650 -CAcreateserial

# We can import root CA cert & our signed certificate
# This should be private and owned by the server
keytool -keystore grpc.keystore.jks -storepass changeit -alias CARoot -import -file root.crt -noprompt
keytool -keystore grpc.keystore.jks -storepass changeit -alias localhost -import -file grpc-signed.crt -noprompt

# This is for clients
keytool -keystore grpc.truststore.jks -storepass changeit -noprompt -alias CARoot -import -file root.crt