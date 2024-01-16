package siemp.estadistica.sigj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import siemp.estadistica.entidades.TAudienciaImputado;
import siemp.estadistica.entidades.TSigjCarpeta;
import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;

public interface AudienciaRepository extends JpaRepository<TAudienciaImputado, Long>{
	
	
	@Query (value= "select * from t_audiencia_imputado " + 
			"where id_tipo_audiencia= :idTipoAudiencia " + 
			"and YEAR(fecha_audiencia)= :anioAudiencia " + 
			"AND MONTH(fecha_audiencia)= :mesAudiencia " + 
			"AND id_tsjdf= :idTsjdf " + 
			"AND estatus<>'0' " + 
			"order by fecha_audiencia desc " + 
			"limit 1;", nativeQuery=true)
	TAudienciaImputado findAudiencia(@Param("idTipoAudiencia") int idTipoAudiencia, 
			@Param("anioAudiencia") int anioAudiencia,
			@Param("mesAudiencia") int mesAudiencia,
			@Param("idTsjdf") Long idTsjdf);
	
	TAudienciaImputado findByIdEvento(Long idEvento);

}
