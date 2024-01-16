package siemp.estadistica.sigj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import siemp.estadistica.entidades.TIncompetencia;
import siemp.estadistica.entidades.TIncompetenciaPK;

public interface IncompetenciaRepository 
		extends JpaRepository<TIncompetencia, TIncompetenciaPK>{

}
