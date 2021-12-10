package com.agungdh.bpkad.sipdtosimdamigrator.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SipdUnit {
    private int id;
    private String namaSkpd;
    private String optionValue;
    private int isSkpd;
    private String kodeSkpd90;
}
