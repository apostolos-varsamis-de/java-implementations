# Motion-Service
## Motion-Service-Write

```
curl -X POST \
-H "Content-Type: application/json" \
-d '{"device": "device1","timestamp": 1641857103,"xPos": 0.0,"yPos": 0.0,"zPos": 2.0,"dist": 22.0}' \
http://localhost:8080/motion-api/v1/motion/projection3d
```
unter windows
```
curl -X POST ^
-H "Content-Type: application/json" ^
-d "{\"device\": \"device1\",\"timestamp\": 1641857103,\"xPos\": 0.0,\"yPos\": 0.0,\"zPos\": 2.0,\"dist\": 22.0}" ^
http://192.168.56.104:8080/motion-api/v1/motion/projection3d
```


## Motion-Service-Read

