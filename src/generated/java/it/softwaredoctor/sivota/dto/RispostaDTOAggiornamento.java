/**
 * @Author: SoftwareDoctor andrea_italiano87@yahoo.com
 * @Date: 2024-07-31 11:30:36
 * @LastEditors: SoftwareDoctor andrea_italiano87@yahoo.com
 * @LastEditTime: 2024-07-31 11:40:32
 * @FilePath: src/generated/java/it/softwaredoctor/sivota/dto/RispostaDTOAggiornamento.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package it.softwaredoctor.sivota.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class RispostaDTOAggiornamento {
    private UUID uuidRisposta; 
    private Boolean isSelected;
}
