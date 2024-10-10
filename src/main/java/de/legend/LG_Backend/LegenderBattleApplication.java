package de.legend.LG_Backend;

import de.legend.LG_Backend.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class LegenderBattleApplication {

	public static void main(String[] args) {
		SpringApplication.run(LegenderBattleApplication.class, args);
	}

}
