package siemp.estadistica.sigj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import siemp.estadistica.entidades.TMedidaVictima;

public interface TMedidaVictimaRepository extends JpaRepository<TMedidaVictima, Long>{
	
	@Query (value= "select * from t_medida_victima vic " + 
			"where vic.id_tsjdf= :id_tsjdf " + 
			"and vic.id_persona= :id_persona " + 
			"and vic.id_tipo_medida= :id_tipo_medida " + 
			"and vic.estatus<>'0';", nativeQuery=true)
	List<TMedidaVictima> consultarRegistroMedida(@Param("id_tsjdf") Long idTsjdf, 
			@Param("id_persona") Long idVictima, @Param("id_tipo_medida") int medida);

}
