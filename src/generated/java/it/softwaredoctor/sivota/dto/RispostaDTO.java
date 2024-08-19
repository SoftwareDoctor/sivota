package it.softwaredoctor.sivota.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import it.softwaredoctor.sivota.dto.DomandaDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import it.softwaredoctor.sivota.model.RispostaVotante;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * RispostaDTO
 */
@JsonTypeName("Risposta")
public class RispostaDTO {

  private UUID uuidRisposta;

  private String testo;

  private DomandaDTO domanda;

  private String dataRisposta;

  private Boolean isSelected;

  private Integer risultatoNumerico;

  @Valid
  private List<RispostaVotante> votanti;

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
  @Schema(name = "uuidRisposta", example = "6e3a4567-912a-4b1f-8d5b-5fb317d1f5ac", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uuidRisposta")
  public UUID getUuidRisposta() {
    return uuidRisposta;
  }

  public void setUuidRisposta(UUID uuidRisposta) {
    this.uuidRisposta = uuidRisposta;
  }

  public RispostaDTO testo(String testo) {
    this.testo = testo;
    return this;
  }

  /**
   * Get testo
   * @return testo
   */
  @Schema(name = "testo", example = "ok", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("testo")
  public String getTesto() {
    return testo;
  }

  public void setTesto(String testo) {
    this.testo = testo;
  }

  public RispostaDTO domanda(DomandaDTO domanda) {
    this.domanda = domanda;
    return this;
  }

  /**
   * Get domanda
   * @return domanda
   */
  @Schema(name = "domanda", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("domanda")
  public DomandaDTO getDomanda() {
    return domanda;
  }

  public void setDomanda(DomandaDTO domanda) {
    this.domanda = domanda;
  }

  public RispostaDTO dataRisposta(String dataRisposta) {
    this.dataRisposta = dataRisposta;
    return this;
  }

  /**
   * Get dataRisposta
   * @return dataRisposta
   */
  @Schema(name = "dataRisposta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dataRisposta")
  public String getDataRisposta() {
    return dataRisposta;
  }

  public void setDataRisposta(String dataRisposta) {
    this.dataRisposta = dataRisposta;
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

  public void setIsSelected(Boolean isSelected) {
    this.isSelected = isSelected;
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

  public void setRisultatoNumerico(Integer risultatoNumerico) {
    this.risultatoNumerico = risultatoNumerico;
  }

  public RispostaDTO votanti(List<RispostaVotante> votanti) {
    this.votanti = votanti;
    return this;
  }

  public RispostaDTO addVotantiItem(RispostaVotante votantiItem) {
    if (this.votanti == null) {
      this.votanti = new ArrayList<>();
    }
    this.votanti.add(votantiItem);
    return this;
  }

  /**
   * Get votanti
   * @return votanti
   */
  @Schema(name = "votanti", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("votanti")
  public List<RispostaVotante> getVotanti() {
    return votanti;
  }

  public void setVotanti(List<RispostaVotante> votanti) {
    this.votanti = votanti;
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
            Objects.equals(this.votanti, risposta.votanti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuidRisposta, testo, domanda, dataRisposta, isSelected, risultatoNumerico, votanti);
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
    sb.append("    votanti: ").append(toIndentedString(votanti)).append("\n");
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
