package siemp.estadistica.sigj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import siemp.estadistica.entidades.TProcedencia;

@Repository
public interface ProcedenciaRepository extends JpaRepository<TProcedencia, Long>{

}
