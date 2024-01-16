package siemp.estadistica.sigj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;
import siemp.estadistica.sigj.intercambio_jpa.TPresuntoResponsable;

public interface PresuntoRepository extends JpaRepository<TPresuntoResponsable, Long>{
	
	List<TPresuntoResponsable> findByIdCarpeta(Long idCarpeta);
	

}
