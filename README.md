An example Java application that will programmatically submit an Apache Storm topology to a Storm cluster. Based on the example from [http://nishutayaltech.blogspot.in/2014/06/submitting-topology-to-remote-storm.html](http://nishutayaltech.blogspot.in/2014/06/submitting-topology-to-remote-storm.html).

##Build and package

1. Install Maven and Java

2. From the command line, change directories to this project root and use the following to build and package:

        mvn package

    This will create a file named __SubmitToNimbus-0.0.1-SNAPSHOT.jar__ in the __target__ directory.

##Use it

1. Get a Storm topology. For example, you can build a basic word count topology following steps at [https://github.com/Blackmist/hdinsight-java-storm-wordcount](https://github.com/Blackmist/hdinsight-java-storm-wordcount).

2. Make sure you know the Nimbus server host node for your Hadoop/Storm cluster and that you can reach it from the machine you are on. For example, `ping hostname` and see if the name resolves. 

    If you can't remotely connect to the host, you'll need to copy both the SubmitToNimbus-0.0.1-SNAPSHOT.jar and the Storm topology (as a .jar file,) to a node on the Hadoop/Storm cluster.
    
3. Use the following to submit the topology using this application:

        java -jar SubmitToNimbus-0.0.1-SNAPSHOT.jar <storm-topology-jar-file> <friendly-name-for-topology> <nimbus-host>
    
    * __&lt;storm-topology-jar-file>__ should be the path to the jar file that contains the topology you want to submit.
    * __&lt;friendly-name-for-topology>__ is a name you make up. Just something so you remember what this topology is.
    * __&lt;nimbus-host>__ is either the host name, fully qualified domain name (FQDN) or IP address of the cluster node that Nimbus is running on.

    If all goes well, you will see a message saying __"Topology successfully submitted."__

If you want to verify that the topology was submitted and is running, you can use the Storm UI for your cluster, or the `storm` command from a command line on the cluster. For example, `storm list` will show the topologies, and the one you submitted should be listed (with the friendly name you provided.)