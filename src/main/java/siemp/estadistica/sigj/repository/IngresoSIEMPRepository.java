package siemp.estadistica.sigj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import siemp.estadistica.entidades.TIngreso;
import siemp.estadistica.entidades.TIngresoPK;

@Repository
public interface IngresoSIEMPRepository extends JpaRepository<TIngreso, TIngresoPK>{
	
	
	@Query (value= "select id_tsjdf " + 
			"from siemp_oral.t_ingreso " + 
			"where num_carpeta_admin= :num_carpeta " + 
			"and anio_carpeta= :anio_carpeta " + 
			"and id_entidad= :id_entidad "+
			"and id_tipo_asunto= :tipo_asunto"
			+ " and estatus<> '0'", nativeQuery=true)
	List<Object[]> consultarExisteCarpeta(@Param("anio_carpeta") int anio, 
				@Param("num_carpeta") int num_carpeta,
				@Param("id_entidad") int id_entidad, 
				@Param("tipo_asunto") int tipo_asunto);
	
	@Query (value= "select count(id_tsjdf) from siemp_oral.t_tsjdf " + 
			"where id_materia=11 " + 
			"and year(fecha_registro)=2023;", nativeQuery=true)
	List<Object[]> consultaFolio();
	
	@Query (value= "select * " + 
			"from t_ingreso " + 
			"where num_carpeta_admin= :num_carpeta " + 
			"and anio_carpeta= :anio_carpeta " + 
			"and id_entidad= :id_entidad"
			+ " and estatus<> '0'", nativeQuery=true)
	List<TIngreso> consultarCarpeta(@Param("anio_carpeta") int anio, 
				@Param("num_carpeta") int num_carpeta,
				@Param("id_entidad") int id_entidad);
	
	
}
