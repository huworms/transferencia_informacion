package siemp.estadistica.sigj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;

@Repository
public interface CarpetasRepository extends JpaRepository<TCarpeta, Long>
{
	
	
	
	
	TCarpeta findByIdCarpeta(Long id);
	
	@Query (value= "select * from transferencia_informacion.t_carpetas " + 
			"where num_investigacion= :numInvestiga " + 
			"and id_unidad_gestion <17 " + 
			" and id_unidad_gestion <> :id_unidad_actual "+
			"order by fecha_solicitud desc;", nativeQuery=true)
	List<TCarpeta> consultarAntecedente(@Param("numInvestiga") String numInvestiga,
			@Param("id_unidad_actual") int id_unidad_actual);
	
	List<TCarpeta> findByIdTipoAudiencia(int tipoAudiencia);
	
	@Query (value= "select * from transferencia_informacion.t_carpetas " + 
			"where id_tipo_audiencia= :tipoAudiencia " + 
			"and YEAR(fecha_solicitud)= :anio " + 
			"AND MONTH(fecha_solicitud)= :mes", nativeQuery=true)
	List<TCarpeta> consultarCarpetas(@Param("anio") int anio, @Param("mes") int mes, 
			@Param("tipoAudiencia") int tipoAudiencia);
	
	
	@Query (value= "select * from transferencia_informacion.t_carpetas " + 
			"where YEAR(fecha_solicitud)= :anio " + 
			"AND MONTH(fecha_solicitud)= :mes", nativeQuery=true)
	List<TCarpeta> consultarCarpetas(@Param("anio") int anio, @Param("mes") int mes);
	
}
