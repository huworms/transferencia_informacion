package siemp.estadistica.sigj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import siemp.estadistica.entidades.TSigjPersona;

@Repository
public interface PersonaSIGJ extends JpaRepository<TSigjPersona, Long>{
	
	@Query (value= "select * from t_sigj_persona " + 
			"where id_persona_sigj= :idPersonaSIGJ " + 
			"and id_tsjdf= :idTsjdf", nativeQuery=true)
	TSigjPersona findByIdPersonaSigj(@Param("idPersonaSIGJ") Long idPersonaSIGJ,
			@Param("idTsjdf") Long idTsjdf);
	
	
	@Query (value= "select * from t_sigj_persona " + 
			"where id_persona_sigj= :idPersonaSIGJ " + 
			"and id_carpeta_sigj= :idCarpeta "+
			"and id_tsjdf= :idTsjdf", nativeQuery=true)
	TSigjPersona findByIdPersonaPre(@Param("idPersonaSIGJ") Long idPersonaSIGJ,
			@Param("idCarpeta") Long idCarpeta, @Param("idTsjdf") Long idTsjdf);

	@Query (value= "select * from t_sigj_persona " + 
			"where id_tsjdf= :idTsjdf", nativeQuery=true)
	List<TSigjPersona> findByIdTsjdfSigj(@Param("idTsjdf") Long idTsjdf);
}
