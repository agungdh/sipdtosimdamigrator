package com.agungdh.bpkad.sipdtosimdamigrator.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SipdSubUnit {
    private int idDataUnit;
    private long idOption;
    private int idSkpd;
    private String kodeSkpd;
    private String namaSkpd;
    private String kodeUnit;
    private String namaUnit;
    private String namaKepala;
    private String nipKepala;
}