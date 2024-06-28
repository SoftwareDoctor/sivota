package it.softwaredoctor.sivota.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "domanda")
@Entity
public class Domanda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "uuid_domanda", unique = true, nullable = false)
    private UUID uuidDomanda;

    private String testo;

    @OneToMany(mappedBy = "domanda", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Risposta> risposte;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "votazione_id")
    private Votazione votazione;

    @PrePersist
    private void onCreateAbstractBaseEntity() {
        this.uuidDomanda = UUID.randomUUID();
    }

//    public Long getId() {
//        return id;
//    }
//
//    public String getTesto() {
//        return testo;
//    }
//
//    public List<Risposta> getRisposte() {
//        return risposte;
//    }
//
//    public Votazione getVotazione() {
//        return votazione;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setUuidDomanda(UUID uuidDomanda) {
//        this.uuidDomanda = uuidDomanda;
//    }
//
//    public void setTesto(String testo) {
//        this.testo = testo;
//    }
//
//    public void setRisposte(List<Risposta> risposte) {
//        this.risposte = risposte;
//    }
//
//    public void setVotazione(Votazione votazione) {
//        this.votazione = votazione;
//    }

//    @Override
//    public String toString() {
//        return "Domanda{" +
//                "id=" + id +
//                ", uuidDomanda=" + uuidDomanda +
//                ", testo='" + testo + '\'' +
//                ", risposte=" + risposte +
//                ", votazione=" + votazione +
//                '}';
//    }
}
