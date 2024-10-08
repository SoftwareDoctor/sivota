package it.softwaredoctor.sivota.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "votazione")
@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Votazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid_votazione", unique = true, nullable = false)
    private UUID uuidVotazione;

    @Column(name="titolo")
    private String titolo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "votazione", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Domanda> domande;

    @ElementCollection
    @CollectionTable(name = "votanti_email", joinColumns = @JoinColumn(name = "votazione_id"))
    @Column(name = "email")
    private List<String> votantiEmail = new ArrayList<>();

    private LocalDate dataCreazione = LocalDate.now();

    @Column(name = "isAnonymous")
    private Boolean isAnonymous;

    @Column(name = "scadenza")
    private LocalDateTime scadenza;

    @PrePersist
    @PreUpdate
    private void onCreateAbstractBaseEntity() {
        this.uuidVotazione = UUID.randomUUID();
        if (this.dataCreazione == null) {
            this.dataCreazione = LocalDate.now();
        }
    }
}
