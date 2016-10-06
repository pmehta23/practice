package com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.stream.Collectors;

public class FilePoller {

	public static void main(String[] args) {

        //define a folder root
        Path myDir = Paths.get("c:/projects/testdata");       

        try {
           WatchService watcher = myDir.getFileSystem().newWatchService();
           myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, 
           StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
           
           WatchKey watckKey = watcher.take();
           while(true){
	           List<WatchEvent<?>> events = watckKey.pollEvents();
	           for (WatchEvent event : events) {
	                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
	                    System.out.println("Created: " + event.context().toString());
	                }
	                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
	                    System.out.println("Delete: " + event.context().toString());
	                }
	                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
	                    System.out.println("Modify: " + event.context().toString());
	                    BufferedReader br = new BufferedReader(new FileReader(myDir+"/"+event.context().toString()));
	                    int size = br.lines().filter(s -> s.contains("stop")).collect(Collectors.toList()).size();
	                    if(size > 0) {
	                    	System.out.println("Encountered stop, exiting");
	                    	System.exit(0);
	                    }else{
	                    	System.out.println("Continuing");
	                    	continue;
	                    }
	                }
	            }
           }
           
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

}
