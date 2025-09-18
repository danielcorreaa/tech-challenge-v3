package com.techchallenge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
//@Component
public class StartLog implements ApplicationListener<ContextRefreshedEvent>{
	
	@Value("${spring.datasource.url}")
	private String urlMsql;
	@Value("${spring.datasource.password}")
	private String senhaMysql;
	@Value("${spring.datasource.username}")
	private String userMysql;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("Url mysql: "+urlMsql);
		log.info("Usuario mysql: "+userMysql);
		log.info("senha mysql: "+senhaMysql);
		
	}

}
