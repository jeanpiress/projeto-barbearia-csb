package com.jeanpiress.ProjetoBarbearia.core.springdoc;

import com.jeanpiress.ProjetoBarbearia.api.exceptionHandlers.Problema;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Configuration
@SecurityScheme(name = "security_auth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(password = @OAuthFlow(
                tokenUrl = "${springdoc.oAuthFlow.tokenUrl}"
        )))
public class SpringDocConfig {

    private static final String badRequestResponse = "BadRequestResponse";
    private static final String noAcceptableResponse = "NoAcceptableResponse";
    private static final String internalServerError = "InternalServerError";


    @Bean
    public OpenAPI openAPI() {
        Map<String, Schema> schemas = gerarSchemas();
        return new OpenAPI().info(new Info()
                        .title("CSB API")
                        .version("V1")
                        .description("REST API do CSB"))
                .components(new Components()
                        .schemas(schemas)
                        .responses(gerarResponses()));
    }



    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            openApi.getPaths()
                    .values()
                    .forEach(pathItem -> pathItem.readOperationsMap()
                            .forEach((httpMethod, operation) -> {
                                ApiResponses responses = operation.getResponses();
                                switch (httpMethod) {
                                    case GET:
                                        responses.addApiResponse("200", new ApiResponse().description("Ok"));
                                        responses.addApiResponse("406", new ApiResponse().$ref(noAcceptableResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerError));
                                        break;

                                    default:
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerError));
                                        break;
                                }
                            }));

        };
    }


    private Map<String, Schema> gerarSchemas() {
        final Map<String, Schema> schemasMap = new HashMap<>();

        // Esquema para a classe Problem
        Schema problemSchema = new Schema();
        problemSchema.addProperties("title", new Schema<String>().example("Dados inválidos"));
        problemSchema.addProperties("detail", new Schema<String>().example("Um ou mais campos estão inválidos"));

        // Esquema para a classe Problem.Field
        Schema problemFieldSchema = new Schema();
        problemFieldSchema.addProperties("name", new Schema<String>().example("nome"));
        problemFieldSchema.addProperties("userMessage", new Schema<String>().example("nome é obrigatório"));

        // Esquema para a lista de campos na classe Problem
        ArraySchema fieldsSchema = new ArraySchema();
        fieldsSchema.setItems(new Schema().$ref("ObjetoProblema"));

        // Adiciona a propriedade "fields" ao esquema da classe Problem
        problemSchema.addProperties("fields", fieldsSchema);

        // Adiciona os esquemas ao mapa
        schemasMap.put("Problema", problemSchema);
        schemasMap.put("ObjetoProblema", problemFieldSchema);

        return schemasMap;
    }

    private Map<String, ApiResponse> gerarResponses() {
        final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

        Content content = new Content().addMediaType(APPLICATION_JSON_VALUE,
                new MediaType().schema(new Schema<Problema>().$ref("Problema")));

        apiResponseMap.put(badRequestResponse, new ApiResponse().description("Requisição invalida").content(content));
        apiResponseMap.put(noAcceptableResponse, new ApiResponse().description("Recurso não possui representação que " +
                "poderia ser aceita pelo consumidor").content(content));
        apiResponseMap.put(internalServerError, new ApiResponse().description("Erro interno do servidor").content(content));

        return apiResponseMap;
    }

}