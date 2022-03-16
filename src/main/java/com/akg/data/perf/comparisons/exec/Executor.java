package com.akg.data.perf.comparisons.exec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.akg.data.perf.comparisons.config.Config;
import com.akg.data.perf.comparisons.service.ElasticsearchCommander;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Executor {
	
	@Autowired
	private Config config;
	
	@Autowired
	private ElasticsearchCommander elasticsearchCommander;
	
	@EventListener(ApplicationReadyEvent.class)
	public void exec() {

		long start = System.currentTimeMillis();
		String what = config.getExecution().getWhat();
		String mode = config.getExecution().getMode();
		log.info( "Executing '{}' '{}' queries for {} ms ...", what, mode, config.getExecutionTimeInMs() );
		if( what.equals( Config.Technology.ELASTICSEARCH.getValue() ) ) {
			if( mode.equals( Config.ExecutionMode.SEARCH.getValue() ) ) {
				elasticsearchCommander.search();
			} else if( mode.equals( Config.ExecutionMode.INSERTS.getValue() ) ) {
				elasticsearchCommander.insert();
			} else if( mode.equals( Config.ExecutionMode.UPDATES.getValue() ) ) {
				elasticsearchCommander.update();
			}
		}
		long end = System.currentTimeMillis();
		log.info("Elapsed Time in seconds: "+ (double)(end-start)/1000);
		System.exit(0);
	}
}
