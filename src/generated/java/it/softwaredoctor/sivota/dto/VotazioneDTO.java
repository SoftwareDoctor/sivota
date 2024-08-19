package it.softwaredoctor.sivota.dto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * VotazioneDTO
 */
@JsonTypeName("Votazione")
public class VotazioneDTO {

    @NotNull
    @Schema(name = "uuidVotazione", example = "6e3a4567-912a-4b1f-8d5b-5fb317d1f5ac")
    @JsonProperty("uuidVotazione")
    private UUID uuidVotazione;

    @Schema(name = "titolo", example = "consiglio_direttivo")
    @JsonProperty("titolo")
    private String titolo;

    @Valid
    @Schema(name = "user")
    @JsonProperty("user")
    private UserDTO user;

    @Valid
    @Schema(name = "domande")
    @JsonProperty("domande")
    private List<DomandaDTO> domande = new ArrayList<>();

    @Valid
    @Schema(name = "votantiEmail")
    @JsonProperty("votantiEmail")
    private List<String> votantiEmail = new ArrayList<>();

    @Schema(name = "dataCreazione")
    @JsonProperty("dataCreazione")
    private LocalDateTime dataCreazione;  // Cambiato a LocalDateTime

    @Schema(name = "isAnonymous", example = "false")
    @JsonProperty("isAnonymous")
    private Boolean isAnonymous;

    @Schema(name = "scadenza")
    @JsonProperty("scadenza")
    private LocalDateTime scadenza;       // Cambiato a LocalDateTime

    public VotazioneDTO() {
        // Default constructor
    }

    public VotazioneDTO uuidVotazione(UUID uuidVotazione) {
        this.uuidVotazione = uuidVotazione;
        return this;
    }

    public UUID getUuidVotazione() {
        return uuidVotazione;
    }

    public void setUuidVotazione(UUID uuidVotazione) {
        this.uuidVotazione = uuidVotazione;
    }

    public VotazioneDTO titolo(String titolo) {
        this.titolo = titolo;
        return this;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public VotazioneDTO user(UserDTO user) {
        this.user = user;
        return this;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public VotazioneDTO domande(List<DomandaDTO> domande) {
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

    public List<DomandaDTO> getDomande() {
        return domande;
    }

    public void setDomande(List<DomandaDTO> domande) {
        this.domande = domande;
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

    public List<String> getVotantiEmail() {
        return votantiEmail;
    }

    public void setVotantiEmail(List<String> votantiEmail) {
        this.votantiEmail = votantiEmail;
    }

    public VotazioneDTO dataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
        return this;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public VotazioneDTO isAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
        return this;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public VotazioneDTO scadenza(LocalDateTime scadenza) {
        this.scadenza = scadenza;
        return this;
    }

    public LocalDateTime getScadenza() {
        return scadenza;
    }

    public void setScadenza(LocalDateTime scadenza) {
        this.scadenza = scadenza;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotazioneDTO that = (VotazioneDTO) o;
        return Objects.equals(uuidVotazione, that.uuidVotazione) &&
                Objects.equals(titolo, that.titolo) &&
                Objects.equals(user, that.user) &&
                Objects.equals(domande, that.domande) &&
                Objects.equals(votantiEmail, that.votantiEmail) &&
                Objects.equals(dataCreazione, that.dataCreazione) &&
                Objects.equals(isAnonymous, that.isAnonymous) &&
                Objects.equals(scadenza, that.scadenza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuidVotazione, titolo, user, domande, votantiEmail, dataCreazione, isAnonymous, scadenza);
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
        sb.append("    scadenza: ").append(toIndentedString(scadenza)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
