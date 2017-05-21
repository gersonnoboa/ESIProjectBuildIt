package com.buildit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ProcurementApplication {
	@Configuration
	static class ObjectMapperCustomizer {
		@Autowired
		@Qualifier("_halObjectMapper")
		private ObjectMapper springHateoasObjectMapper;

        //unused
		@Bean(name = "objectMapper")
		ObjectMapper objectMapper() {
			return springHateoasObjectMapper
					//.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
					.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
					.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
					.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
					.registerModules(new JavaTimeModule())
                    .registerModule(new Jackson2HalModule());
		}
		@Bean
		public RestTemplate restTemplate() {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new Jackson2HalModule());
            RestTemplate _restTemplate= new RestTemplate();
			return _restTemplate;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ProcurementApplication.class, args);
	}
}
