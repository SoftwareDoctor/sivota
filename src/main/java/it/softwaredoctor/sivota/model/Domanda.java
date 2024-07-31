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

    @JsonIgnore
    @OneToMany(mappedBy = "domanda", cascade = CascadeType.ALL, orphanRemoval = true)
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

}
