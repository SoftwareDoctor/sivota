package it.softwaredoctor.sivota.dto;

import java.net.URI;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import it.softwaredoctor.sivota.dto.VotazioneDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;

import jakarta.annotation.Generated;

/**
 * UserDTO
 */

@JsonTypeName("User")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-19T10:22:51.442175600+02:00[Europe/Rome]")
public class UserDTO {

    private UUID uuidUser;

    private String username;

    private String password;

    private String email;

    private String name;

    private String surname;

    @Valid
    private List<@Valid VotazioneDTO> votazione;

    private Boolean registrato;

    private String roleName;

    public UserDTO uuidUser(UUID uuidUser) {
        this.uuidUser = uuidUser;
        return this;
    }

    /**
     * Get uuidUser
     *
     * @return uuidUser
     */
    @Valid
    @Schema(name = "uuidUser", example = "6e3a4567-912a-4b1f-8d5b-5fb317d1f5ac", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("uuidUser")
    public UUID getUuidUser() {
        return uuidUser;
    }

    public UserDTO username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Get username
     *
     * @return username
     */

    @Schema(name = "username", example = "andrea87", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public UserDTO password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Get password
     *
     * @return password
     */

    @Schema(name = "password", example = "andrea87", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public UserDTO email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Get email
     *
     * @return email
     */

    @Schema(name = "email", example = "andrea87@yahoo.it", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public UserDTO name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     */

    @Schema(name = "name", example = "andrea", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public UserDTO surname(String surname) {
        this.surname = surname;
        return this;
    }

    /**
     * Get surname
     *
     * @return surname
     */

    @Schema(name = "surname", example = "italiano", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("surname")
    public String getSurname() {
        return surname;
    }

    public UserDTO votazione(List<@Valid VotazioneDTO> votazione) {
        this.votazione = votazione;
        return this;
    }

    public UserDTO addVotazioneItem(VotazioneDTO votazioneItem) {
        if (this.votazione == null) {
            this.votazione = new ArrayList<>();
        }
        this.votazione.add(votazioneItem);
        return this;
    }

    /**
     * Get votazione
     *
     * @return votazione
     */
    @Valid
    @Schema(name = "votazione", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("votazione")
    public List<@Valid VotazioneDTO> getVotazione() {
        return votazione;
    }

    public UserDTO registrato(Boolean registrato) {
        this.registrato = registrato;
        return this;
    }

    /**
     * Get registrato
     *
     * @return registrato
     */

    @Schema(name = "registrato", example = "false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("registrato")
    public Boolean getRegistrato() {
        return registrato;
    }


  public UserDTO roleName(String roleName) {
    this.roleName = roleName;
    return this;
  }

  /**
   * Get roleName
   *
   * @return roleName
   */

  @Schema(name = "roleName", example = "false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roleName")
  public String getRoleName() {
    return roleName;
  }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDTO user = (UserDTO) o;
        return Objects.equals(this.uuidUser, user.uuidUser) &&
                Objects.equals(this.username, user.username) &&
                Objects.equals(this.password, user.password) &&
                Objects.equals(this.email, user.email) &&
                Objects.equals(this.name, user.name) &&
                Objects.equals(this.surname, user.surname) &&
                Objects.equals(this.votazione, user.votazione) &&
                Objects.equals(this.registrato, user.registrato) &&
                Objects.equals(this.roleName, user.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuidUser, username, password, email, name, surname, votazione, registrato, roleName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserDTO {\n");
        sb.append("    uuidUser: ").append(toIndentedString(uuidUser)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
        sb.append("    votazione: ").append(toIndentedString(votazione)).append("\n");
        sb.append("    registrato: ").append(toIndentedString(registrato)).append("\n");
        sb.append("    roleName: ").append(toIndentedString(roleName)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

