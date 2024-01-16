package siemp.estadistica.sigj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import siemp.estadistica.entidades.TActoProcesal;
import siemp.estadistica.entidades.TAudienciaImputado;

public interface ActoProcesalRepository extends JpaRepository<TActoProcesal, Long>{
	
	@Query (value= "select * from t_acto_procesal " + 
			"where id_tipo_acto= :idTipoActo " + 
			"and id_evento= :idEvento " + 
			"AND id_persona= :idPersona " + 
			"AND estatus<>'0' "+ 
			"limit 1;", nativeQuery=true)
	TActoProcesal findActoProcesal(@Param("idTipoActo") int idTipoActo, 
			@Param("idEvento") Long idEvento,
			@Param("idPersona") Long idPersona);

}
