package siemp.estadistica.sigj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import siemp.estadistica.entidades.TPersona;
import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;

public interface PersonaRepository extends JpaRepository<TPersona, Long>{
	
	@Query (value= "select sexo, count(id_persona) as hit " + 
			" from t_persona " + 
			" where nombre = :nombre " + 
			" and sexo in ('F','M') " + 
			" group by sexo ", nativeQuery=true)
	List<Object[]> consultaSexoNombre(@Param("nombre") String nombre);
	
	
	@Query (value= "select * " + 
			" from t_persona " + 
			" where id_persona = :idPersona ", nativeQuery=true)
	TPersona findByIdPersona(@Param("idPersona") Long id);

}
