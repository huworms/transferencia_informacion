package siemp.estadistica.sigj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import siemp.estadistica.sigj.intercambio_jpa.TMedidaCnpp;

public interface MedidaProteccionCNPP extends JpaRepository<TMedidaCnpp, Long>{
	
	List<TMedidaCnpp> findByIdPersona(Long idPersonaSIGJ);

}
