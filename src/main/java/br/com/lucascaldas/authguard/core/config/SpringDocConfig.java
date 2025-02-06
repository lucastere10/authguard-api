package br.com.lucascaldas.authguard.core.config;


import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.lucascaldas.authguard.api.exceptionhandler.Problem;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.media.Content;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@io.swagger.v3.oas.annotations.security.SecurityScheme(
  name = "Bearer Authentication",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer"
)
public class SpringDocConfig {

        private static final String badRequestResponse = "BadRequestResponse";
        private static final String notFoundResponse = "NotFoundResponse";
        private static final String internalServerErrorResponse = "InternalServerErrorResponse";

        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")))
                                .info(new Info()
                                                .title("Authguard")
                                                .version("0.1v")
                                                .description(
                                                                "AuthGuard is a tech demo project with the intend to build a modern authentication system that supports multiple authentication methods, including social login, email token verification, and QR code authentication. It provides a seamless and secure user experience across web and mobile platforms.")
                                                .contact(new Contact()
                                                                .name("Lucas Caldas")
                                                                .email("lucastere10@gmail.com"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("http://authguard.com")))
                                .externalDocs(new ExternalDocumentation()
                                                .description("Authguard")
                                                .url("http://authguard.com"))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .components(new Components()
                                                .schemas(gerarSchemas())
                                                .responses(gerarResponses()));
        }

        @Bean
        public OpenApiCustomizer openApiCustomizer() {
            return openApi -> {
                openApi.getPaths()
                        .values()
                        .forEach(pathItem -> pathItem.readOperationsMap()
                                .forEach((httpMethod, operation) -> {
                                    ApiResponses responses = operation.getResponses();
                                    switch (httpMethod) {
                                        case GET:
                                            responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                            break;
                                        case POST:
                                            responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
                                            responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                            break;
                                        case PUT:
                                            responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
                                            responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                            break;
                                        case DELETE:
                                            responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                            break;
                                        default:
                                            responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                            break;
                                    }
                                })
                        );
            };
        }

        private Map<String, Schema> gerarSchemas() {
                final Map<String, Schema> schemaMap = new HashMap<>();

                Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);
                Map<String, Schema> problemObjectSchema = ModelConverters.getInstance().read(Problem.Object.class);

                schemaMap.putAll(problemSchema);
                schemaMap.putAll(problemObjectSchema);

                return schemaMap;
        }

        private Map<String, ApiResponse> gerarResponses() {
                final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

                Content content = new Content()
                                .addMediaType(APPLICATION_JSON_VALUE,
                                                new MediaType().schema(new Schema<Problem>().$ref("Problema")));

                apiResponseMap.put(badRequestResponse, new ApiResponse()
                                .description("Requisição inválida")
                                .content(content));

                apiResponseMap.put(notFoundResponse, new ApiResponse()
                                .description("Recurso não encontrado")
                                .content(content));

                apiResponseMap.put(internalServerErrorResponse, new ApiResponse()
                                .description("Erro interno no servidor")
                                .content(content));

                return apiResponseMap;
        }
}