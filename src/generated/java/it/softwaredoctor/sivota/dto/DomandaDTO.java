package it.softwaredoctor.sivota.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import it.softwaredoctor.sivota.dto.RispostaDTO;
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
 * DomandaDTO
 */


@JsonTypeName("Domanda")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-06-19T10:22:51.442175600+02:00[Europe/Rome]")
public class DomandaDTO {

  private UUID uuidDomanda;

  private String testo;

  @Valid
  private List<@Valid RispostaDTO> risposte = new ArrayList<>();

  private VotazioneDTO votazione;



  public DomandaDTO() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DomandaDTO(UUID uuidDomanda, String testo, List<@Valid RispostaDTO> risposte, VotazioneDTO votazione) {
    this.uuidDomanda = uuidDomanda;
    this.testo = testo;
    this.risposte = risposte;
    this.votazione = votazione;
  }

  public DomandaDTO uuidDomanda(UUID uuidDomanda) {
    this.uuidDomanda = uuidDomanda;
    return this;
  }

  /**
   * Get uuidDomanda
   * @return uuidDomanda
  */
  @NotNull @Valid
  @Schema(name = "uuidDomanda", example = "6e3a4567-912a-4b1f-8d5b-5fb317d1f5ac", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uuidDomanda")
  public UUID getUuidDomanda() {
    return uuidDomanda;
  }

    public DomandaDTO testo(String testo) {
    this.testo = testo;
    return this;
  }

  /**
   * Get testo
   * @return testo
  */
  @NotNull
  @Schema(name = "testo", example = "Qual è la tua opinione?", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("testo")
  public String getTesto() {
    return testo;
  }

    public DomandaDTO risposte(List<@Valid RispostaDTO> risposte) {
    this.risposte = risposte;
    return this;
  }

  public DomandaDTO addRisposteItem(RispostaDTO risposteItem) {
    if (this.risposte == null) {
      this.risposte = new ArrayList<>();
    }
    this.risposte.add(risposteItem);
    return this;
  }

  /**
   * Get risposte
   * @return risposte
  */
  @NotNull @Valid
  @Schema(name = "risposte", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("risposte")
  public List<@Valid RispostaDTO> getRisposte() {
    return risposte;
  }

    public DomandaDTO votazione(VotazioneDTO votazione) {
    this.votazione = votazione;
    return this;
  }

  /**
   * Get votazione
   * @return votazione
  */
  @NotNull @Valid
  @Schema(name = "votazione", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("votazione")
  public VotazioneDTO getVotazione() {
    return votazione;
  }


    @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DomandaDTO domanda = (DomandaDTO) o;
    return Objects.equals(this.uuidDomanda, domanda.uuidDomanda) &&
        Objects.equals(this.testo, domanda.testo) &&
        Objects.equals(this.risposte, domanda.risposte) &&
        Objects.equals(this.votazione, domanda.votazione);

  }

  @Override
  public int hashCode() {
    return Objects.hash(uuidDomanda, testo, risposte, votazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DomandaDTO {\n");
    sb.append("    uuidDomanda: ").append(toIndentedString(uuidDomanda)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
    sb.append("    risposte: ").append(toIndentedString(risposte)).append("\n");
    sb.append("    votazione: ").append(toIndentedString(votazione)).append("\n");
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

