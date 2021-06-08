# Produce and Consume Messages :

```sh
----------------------------------
# Add Queue Configurations in "broker.xml":

<addresses>
    <!-- Sample Queues -->
    <address name="SampleQueues">
        <anycast>
            <queue name="q1" />
            <queue name="q2" />
        </anycast>
    </address>
</addresses>

```