# Red Hat CodeReady Studio v12.17.0.GA Workspace :

# AMQ Broker Clients :

```sh
---------------------------------------------
# amq-broker-client:

$ java com.xtrim.amqbroker.ArtemisQueueDemo
$ java com.xtrim.amqbroker.ClusteredQueueExample

# helloworld-mdb-consumer:

$ mvn clean package
/targets$ cp helloworld-mdb-consumer.jar /jboss-eap-7.2/standalone/deployments/

# helloworld-mdb-producer:

$ mvn clean package
/targets$ cp helloworld-mdb-producer.war /jboss-eap-7.2/standalone/deployments/

```