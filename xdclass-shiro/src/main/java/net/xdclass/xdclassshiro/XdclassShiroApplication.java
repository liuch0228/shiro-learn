package net.xdclass.xdclassshiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//扫描mapper包
@MapperScan("net.xdclass.xdclassshiro.dao")
public class XdclassShiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(XdclassShiroApplication.class, args);
	}

}
