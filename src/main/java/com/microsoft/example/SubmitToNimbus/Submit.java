package com.microsoft.example.SubmitToNimbus;

import java.util.Map;

import org.apache.thrift7.TException;
import org.apache.thrift7.transport.TTransportException;
import org.json.simple.JSONValue;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
//import backtype.storm.generated.Nimbus.Client;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.NimbusClient;
import backtype.storm.utils.Utils;

public class Submit 
{
    public static void main( String[] args )
    {
    	//Get parameters for the jar file, topology name, and target Nimbus host node
    	if(args.length < 3) {
    		System.err.println("Invalid arguments. Usage:");
    		System.err.println("submit stormtopology.jar topologyname nimbusnode");
    		System.exit(1);
    	}
    	String topologyFile = args[0];
    	String topologyName = args[1];
    	String nimbusNode = args[2];
    	
    	//Create a builder and configurations.
    	TopologyBuilder builder = new TopologyBuilder();
        Config config = new Config();
        config.put(Config.NIMBUS_HOST, nimbusNode);
        config.setDebug(true);
        
        Map stormConfig = Utils.readStormConfig();
        stormConfig.put("nimbus.host", nimbusNode);
        
        //Get a client using the configurations
        //Client client = NimbusClient.getConfiguredClient(stormConfig)
        //		.getClient();

        NimbusClient nimbus;
		try {
			nimbus = new NimbusClient(stormConfig, nimbusNode, 6627);
		
			String submittedJar = StormSubmitter.submitJar(stormConfig, topologyFile);
        
	        try {
	        	String jsonConfig = JSONValue.toJSONString(stormConfig);
	        	nimbus.getClient().submitTopology(topologyName, submittedJar, jsonConfig, builder.createTopology());
	        } catch(AlreadyAliveException e) {
	        	System.err.println("An instance of the topology is already running.");
	        	System.exit(1);;
	        } catch (InvalidTopologyException e) {
				System.err.println("The topology is invalid.");
				System.exit(1);
			} catch (TException e) {
				System.err.println("An error occured submitting the topology.");
				e.printStackTrace();
				System.exit(1);
			}
		} catch (TTransportException e) {
			System.err.println("There was an error connecting to the Nimbus host node.");
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Topology successfully submitted.");
        System.exit(0);
    }
}
