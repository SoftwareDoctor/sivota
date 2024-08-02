/**
 * @Author: SoftwareDoctor andrea_italiano87@yahoo.com
 * @Date: 2024-08-02 07:08:55
 * @LastEditors: SoftwareDoctor andrea_italiano87@yahoo.com
 * @LastEditTime: 2024-08-02 07:11:25
 * @FilePath: src/main/java/it/softwaredoctor/sivota/repository/RispostaVotanteRepository.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package it.softwaredoctor.sivota.repository;

import it.softwaredoctor.sivota.model.RispostaVotante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RispostaVotanteRepository extends JpaRepository<RispostaVotante, Long> {
}
