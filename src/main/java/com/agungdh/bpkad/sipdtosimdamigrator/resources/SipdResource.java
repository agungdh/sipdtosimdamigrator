package com.agungdh.bpkad.sipdtosimdamigrator.resources;

import com.agungdh.bpkad.sipdtosimdamigrator.models.SipdUnit;
import com.agungdh.bpkad.sipdtosimdamigrator.services.SipdService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sipd")
@RequiredArgsConstructor
public class SipdResource {
    @Autowired
    SipdService sipdService;

//    @GetMapping("/listopd")
//    public void listopd() {
//        sipdService.getSipdOptions();
//    }

    @GetMapping("/listopdunitonly")
    public ResponseEntity<List<SipdUnit>> listopdunitonly() {
        List<SipdUnit> listopdunitonlys = sipdService.getSipdOptionsBasedOnUnit();

        return ResponseEntity.ok().body(listopdunitonlys);
    }

    @GetMapping("/updateSubUnitValue")
    public void updateSubUnitValue() {
        sipdService.updateSubUnitValue();
    }

    @GetMapping("/injectsimdaskpd")
    public void injectsimdaskpd() {
        sipdService.simdaInjectRefUnitSkpd();
    }
}
