package hr.java.web.radanovic.recordkeeper.configuration;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public Docket recordKeeperApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().apiInfo(getApiUnfo());
	}

	private ApiInfo getApiUnfo() {
		return new ApiInfoBuilder().title("Record Keeper Api").version("1.0").description("Api for Record Keeper App")
				.contact(new Contact("Radanovic", "https://github.com/dradanovi", "dradanovi@gmail.com"))
				.license("Apache License Version 2.0").build();
	}

}
