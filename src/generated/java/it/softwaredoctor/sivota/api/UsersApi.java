/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package it.softwaredoctor.sivota.api;

import it.softwaredoctor.sivota.dto.UserDTO;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-08-02T08:41:40.585158500+02:00[Europe/Rome]")
@Validated
@Tag(name = "users", description = "the users API")
public interface UsersApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /users : Creare un nuovo utente
     *
     * @param userDTO  (required)
     * @return Utente creato con successo (status code 200)
     *         or Richiesta non valida (status code 400)
     */
    @Operation(
        operationId = "createUser",
        summary = "Creare un nuovo utente",
        tags = { "users" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Utente creato con successo"),
            @ApiResponse(responseCode = "400", description = "Richiesta non valida")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/users",
        consumes = { "application/json" }
    )

    default ResponseEntity<Void> createUser(
        @Parameter(name = "UserDTO", description = "", required = true) @Valid @RequestBody UserDTO userDTO
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
