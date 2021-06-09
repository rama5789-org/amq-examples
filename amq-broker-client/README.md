# Produce and Consume Messages :

```xml
----------------------------------
# Configurations in "broker.xml":

<core> 
    <!-- ###### TCP Cluster Configurations (Static Discovery) ###### -->
    <connectors>
        <connector name="netty-connector">tcp://172.26.16.1:61616</connector>
        <connector name="broker2-connector">tcp://172.26.16.1:61716</connector>
    </connectors>
    <acceptors>
        <acceptor name="netty-acceptor">tcp://172.26.16.1:61616</acceptor>
    </acceptors>
    <cluster-connections>
        <cluster-connection name="amq-cluster">
        <connector-ref>netty-connector</connector-ref>
        <retry-interval>500</retry-interval>
        <use-duplicate-detection>true</use-duplicate-detection>
        <message-load-balancing>STRICT</message-load-balancing>
        <max-hops>1</max-hops>
        <static-connectors>
            <connector-ref>broker2-connector</connector-ref>
        </static-connectors>
        </cluster-connection>
    </cluster-connections>

    <!-- ###### (TCP|UDP) Cluster Credentials ###### -->
    <cluster-user>amq_cluster_user</cluster-user>
    <cluster-password>Abcd1234</cluster-password>

    <!-- ###### Other Configurations ###### -->
    <configuration-file-refresh-period>60000</configuration-file-refresh-period>

    <!-- ###### Queue Configurations ###### -->
    <addresses>
        <!-- Queues -->
        <address name="SampleQ">
            <anycast>
               <queue name="SampleQ"/>
            </anycast>
         </address>
    </addresses>
</core> 
```