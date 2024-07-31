package it.softwaredoctor.sivota.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "risposta")
public class Risposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid_risposta", unique = true, nullable = false)
    private UUID uuidRisposta;

    private String testo;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "domanda_id")
    private Domanda domanda;

//    @Builder.Default
    private LocalDate dataRisposta = LocalDate.now();

    private Boolean isSelected;

    private Integer risultatoNumerico;

    @ElementCollection
    @CollectionTable(name = "risposta_votanti_email", joinColumns = @JoinColumn(name = "risposta_id"))
    @Column(name = "email")
    private List<String> votantiEmail = new ArrayList<>();


    @PrePersist
    @PreUpdate
    private void ensureFieldsAreNotNull() {
        if (this.uuidRisposta == null) {
            this.uuidRisposta = UUID.randomUUID();
        }
        if (this.isSelected == null) {
            this.isSelected = false;
        }
        if(this.risultatoNumerico == null) {
            this.risultatoNumerico = 0;
        }
        if(this.dataRisposta == null && !this.getIsSelected()) {
            this.dataRisposta = LocalDate.now();
        } else {
            this.dataRisposta = null;
        }
    }

}

