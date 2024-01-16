package siemp.estadistica.sigj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import siemp.estadistica.sigj.intercambio_jpa.TTVictima;

public interface VictimaRepository extends JpaRepository<TTVictima, Long>{
	
	List<TTVictima> findByIdCarpeta(Long idCarpeta);

}
