# Ces Vector

## Modules

* **servector**: Serves clients to manage manipulation of a vector array 
* **sertransactionmanager**: Serves clients and VectorServices to manage transactions initiated by clients, and branches registered by VectorServices 
* **serlockmanager**: Serves clients to manage locked elements in Vector Service

## Development
```
cd <module>
mvn compile quarkus:dev
```

## Deployment

1. Packaging Service to run in a Docker Container
```
cd serlockmanager
mvn clean package
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/serlockmanager-jvm .
docker tag serlockmanager:v1 wilsonrcosta/iesd:serlockmanager
docker push wilsonrcosta/iesd:serlockmanager 
//docker run -i --rm -p 8080:8080 quarkus/serlockmanager-jvm
```

2. Start Minikube cluster (choose your driver)
```
minikube start --driver=docker --alsologtostderr
minikube addons enable ingress
```

3. Run Kubernetes configuration YAML files
```
kubectl apply -f <FILE>
...
```

4. Add host name resolution
```
sudo vim /etc/hosts
```
4.1 Add the following to the Host database (**if using Mac with Docker driver - 127.0.0.1 instead**) 
* 192.168.49.2    isos.iesd.pt

5. Start minikube tunnel
```
sudo minikube tunnel --alsologtostderr
```