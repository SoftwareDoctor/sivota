package it.softwaredoctor.sivota.dto;

import java.net.URI;
import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import it.softwaredoctor.sivota.dto.DomandaDTO;
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
 * RispostaDTO
 */

@JsonTypeName("Risposta")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-19T10:22:51.442175600+02:00[Europe/Rome]")
public class RispostaDTO {

  private UUID uuidRisposta;

  private String testo;

  private DomandaDTO domanda;

  private LocalDate dataRisposta;

  private Boolean isSelected;

  private Integer risultatoNumerico;

  @Valid
  private List<String> votantiEmail;

  public RispostaDTO() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RispostaDTO(UUID uuidRisposta, String testo, DomandaDTO domanda) {
    this.uuidRisposta = uuidRisposta;
    this.testo = testo;
    this.domanda = domanda;
  }

  public RispostaDTO uuidRisposta(UUID uuidRisposta) {
    this.uuidRisposta = uuidRisposta;
    return this;
  }

  /**
   * Get uuidRisposta
   * @return uuidRisposta
  */
  @NotNull @Valid
  @Schema(name = "uuidRisposta", example = "6e3a4567-912a-4b1f-8d5b-5fb317d1f5ac", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uuidRisposta")
  public UUID getUuidRisposta() {
    return uuidRisposta;
  }

    public RispostaDTO testo(String testo) {
    this.testo = testo;
    return this;
  }

  /**
   * Get testo
   * @return testo
  */
  @NotNull
  @Schema(name = "testo", example = "ok", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("testo")
  public String getTesto() {
    return testo;
  }

    public RispostaDTO domanda(DomandaDTO domanda) {
    this.domanda = domanda;
    return this;
  }

  /**
   * Get domanda
   * @return domanda
  */
  @NotNull @Valid
  @Schema(name = "domanda", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("domanda")
  public DomandaDTO getDomanda() {
    return domanda;
  }

    public RispostaDTO dataRisposta(LocalDate dataRisposta) {
    this.dataRisposta = dataRisposta;
    return this;
  }

  /**
   * Get dataRisposta
   * @return dataRisposta
  */

  @Schema(name = "dataRisposta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dataRisposta")
  public LocalDate getDataRisposta() {
    return dataRisposta;
  }

    public RispostaDTO isSelected(Boolean isSelected) {
    this.isSelected = isSelected;
    return this;
  }

  /**
   * Get isSelected
   * @return isSelected
  */

  @Schema(name = "isSelected", example = "false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isSelected")
  public Boolean getIsSelected() {
    return isSelected;
  }

    public RispostaDTO risultatoNumerico(Integer risultatoNumerico) {
    this.risultatoNumerico = risultatoNumerico;
    return this;
  }

  /**
   * Get risultatoNumerico
   * @return risultatoNumerico
  */

  @Schema(name = "risultatoNumerico", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risultatoNumerico")
  public Integer getRisultatoNumerico() {
    return risultatoNumerico;
  }

    public RispostaDTO votantiEmail(List<String> votantiEmail) {
    this.votantiEmail = votantiEmail;
    return this;
  }

  public RispostaDTO addVotantiEmailItem(String votantiEmailItem) {
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

    @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RispostaDTO risposta = (RispostaDTO) o;
    return Objects.equals(this.uuidRisposta, risposta.uuidRisposta) &&
        Objects.equals(this.testo, risposta.testo) &&
        Objects.equals(this.domanda, risposta.domanda) &&
        Objects.equals(this.dataRisposta, risposta.dataRisposta) &&
        Objects.equals(this.isSelected, risposta.isSelected) &&
        Objects.equals(this.risultatoNumerico, risposta.risultatoNumerico) &&
        Objects.equals(this.votantiEmail, risposta.votantiEmail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuidRisposta, testo, domanda, dataRisposta, isSelected, risultatoNumerico, votantiEmail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RispostaDTO {\n");
    sb.append("    uuidRisposta: ").append(toIndentedString(uuidRisposta)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
    sb.append("    domanda: ").append(toIndentedString(domanda)).append("\n");
    sb.append("    dataRisposta: ").append(toIndentedString(dataRisposta)).append("\n");
    sb.append("    isSelected: ").append(toIndentedString(isSelected)).append("\n");
    sb.append("    risultatoNumerico: ").append(toIndentedString(risultatoNumerico)).append("\n");
    sb.append("    votantiEmail: ").append(toIndentedString(votantiEmail)).append("\n");
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

