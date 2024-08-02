/**
 * @Author: SoftwareDoctor andrea_italiano87@yahoo.com
 * @Date: 2024-08-02 06:54:18
 * @LastEditors: SoftwareDoctor andrea_italiano87@yahoo.com
 * @LastEditTime: 2024-08-02 06:59:27
 * @FilePath: src/main/java/it/softwaredoctor/sivota/model/RispostaVotante.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package it.softwaredoctor.sivota.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "risposta_votanti_email")
@Data
@Entity
public class RispostaVotante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRispostaVotante;

    @ManyToOne
    @JoinColumn(name = "risposta_id", nullable = false)
    private Risposta risposta;

    @Column(name = "email", nullable = false)
    private String email;
}
