Instructions : 

```bash
$ brew install protobuf 
$ brew install go
$ go get -u github.com/golang/protobuf/{proto,protoc-gen-go}
$ go get -u github.com/grpc-ecosystem/grpc-gateway/protoc-gen-grpc-gateway
$ go get -u github.com/grpc-ecosystem/grpc-gateway/protoc-gen-swagger
$ go get -u github.com/golang/protobuf/protoc-gen-go
```

Make sure that `$GOPATH/bin` is in your path. 

The reason that you have to install go is that the HTTP/REST gateway is 
written in go. 

Run `sbt compile`. This will generate all the stubs (scala and go) from the proto definition 
that lives in `src/main/protobuf/protocol.proto` (Note that this naming is terrible),
which defines the datatypes and the API methods. 

Run `sbt run` and select the server to run the gRPC server written in Scala. 

Run `go run gateway/server/test.go` to run the gateway server. 

Run the following curl command to perform a request from the gateway

```
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -H "Postman-Token: 1e813409-6aa6-8cd1-70be-51305f31667f" -d '{
     	"password" : "password"
     }' "http://127.0.0.1:8080/v1/johndoe64"
```

Run `sbt run` and select the client to run a native gRPC request against the gRPC server. 

