package it.softwaredoctor.sivota.dto;

import java.net.URI;
import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import it.softwaredoctor.sivota.dto.DomandaDTO;
import it.softwaredoctor.sivota.dto.UserDTO;
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
 * VotazioneDTO
 */
@JsonTypeName("Votazione")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-19T10:22:51.442175600+02:00[Europe/Rome]")
public class VotazioneDTO {

  private UUID uuidVotazione;

  private String titolo;

  private UserDTO user;

  @Valid
  private List<@Valid DomandaDTO> domande;

  @Valid
  private List<String> votantiEmail;

  private LocalDate dataCreazione;

  private Boolean isAnonymous;

  public VotazioneDTO uuidVotazione(UUID uuidVotazione) {
    this.uuidVotazione = uuidVotazione;
    return this;
  }

  /**
   * Get uuidVotazione
   * @return uuidVotazione
  */
  @Valid
  @Schema(name = "uuidVotazione", example = "6e3a4567-912a-4b1f-8d5b-5fb317d1f5ac", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uuidVotazione")
  public UUID getUuidVotazione() {
    return uuidVotazione;
  }

    public VotazioneDTO titolo(String titolo) {
    this.titolo = titolo;
    return this;
  }

  /**
   * Get titolo
   * @return titolo
  */
  @Schema(name = "titolo", example = "consiglio_direttivo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("titolo")
  public String getTitolo() {
    return titolo;
  }

    public VotazioneDTO user(UserDTO user) {
    this.user = user;
    return this;
  }

  /**
   * Get user
   * @return user
  */
  @Valid
  @Schema(name = "user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("user")
  public UserDTO getUser() {
    return user;
  }

    public VotazioneDTO domande(List<@Valid DomandaDTO> domande) {
    this.domande = domande;
    return this;
  }

  public VotazioneDTO addDomandeItem(DomandaDTO domandeItem) {
    if (this.domande == null) {
      this.domande = new ArrayList<>();
    }
    this.domande.add(domandeItem);
    return this;
  }

  /**
   * Get domande
   * @return domande
  */
  @Valid
  @Schema(name = "domande", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("domande")
  public List<@Valid DomandaDTO> getDomande() {
    return domande;
  }

    public VotazioneDTO votantiEmail(List<String> votantiEmail) {
    this.votantiEmail = votantiEmail;
    return this;
  }

  public VotazioneDTO addVotantiEmailItem(String votantiEmailItem) {
    if (this.votantiEmail == null) {
      this.votantiEmail = new ArrayList<>();
    }
    this.votantiEmail.add(votantiEmailItem);
    return this;
  }

  /**
   * Get votantiEmail
   * @return votantiEmail
  */

  @Schema(name = "votantiEmail", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("votantiEmail")
  public List<String> getVotantiEmail() {
    return votantiEmail;
  }

    public VotazioneDTO dataCreazione(LocalDate dataCreazione) {
    this.dataCreazione = dataCreazione;
    return this;
  }

  /**
   * Get dataCreazione
   * @return dataCreazione
  */

  @Schema(name = "dataCreazione", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dataCreazione")
  public LocalDate getDataCreazione() {
    return dataCreazione;
  }

    public VotazioneDTO isAnonymous(Boolean isAnonymous) {
    this.isAnonymous = isAnonymous;
    return this;
  }

  /**
   * Get isAnonymous
   * @return isAnonymous
  */

  @Schema(name = "isAnonymous", example = "false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isAnonymous")
  public Boolean getIsAnonymous() {
    return isAnonymous;
  }

    @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VotazioneDTO votazione = (VotazioneDTO) o;
    return Objects.equals(this.uuidVotazione, votazione.uuidVotazione) &&
        Objects.equals(this.titolo, votazione.titolo) &&
        Objects.equals(this.user, votazione.user) &&
        Objects.equals(this.domande, votazione.domande) &&
        Objects.equals(this.votantiEmail, votazione.votantiEmail) &&
        Objects.equals(this.dataCreazione, votazione.dataCreazione) &&
        Objects.equals(this.isAnonymous, votazione.isAnonymous);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuidVotazione, titolo, user, domande, votantiEmail, dataCreazione, isAnonymous);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VotazioneDTO {\n");
    sb.append("    uuidVotazione: ").append(toIndentedString(uuidVotazione)).append("\n");
    sb.append("    titolo: ").append(toIndentedString(titolo)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    domande: ").append(toIndentedString(domande)).append("\n");
    sb.append("    votantiEmail: ").append(toIndentedString(votantiEmail)).append("\n");
    sb.append("    dataCreazione: ").append(toIndentedString(dataCreazione)).append("\n");
    sb.append("    isAnonymous: ").append(toIndentedString(isAnonymous)).append("\n");
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

