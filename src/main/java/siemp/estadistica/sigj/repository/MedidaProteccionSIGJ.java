package siemp.estadistica.sigj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import siemp.estadistica.sigj.intercambio_jpa.TMedidaLamvlv;

public interface MedidaProteccionSIGJ extends JpaRepository<TMedidaLamvlv, Long>{
	
	List<TMedidaLamvlv> findByIdPersona(Long idPersonaSIGJ);
	

}
