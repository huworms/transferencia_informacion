package siemp.estadistica.sigj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import siemp.estadistica.entidades.TPersona;
import siemp.estadistica.entidades.TSituacionPersona;
import siemp.estadistica.entidades.TSituacionPersonaPK;

public interface SituacionRepository extends JpaRepository<TSituacionPersona, TSituacionPersonaPK>{

	@Query (value= "select * " + 
			" from siemp_oral.t_situacion_persona " + 
			" where id_tsjdf= :idTsjdf" + 
			" and estatus='1'", nativeQuery=true)
	public List<TSituacionPersona> findByIDTsjdf(@Param("idTsjdf") long idTsjdf);
	
	@Query (value= "select * " + 
			" from siemp_oral.t_situacion_persona " + 
			" where id_tsjdf= :idTsjdf" + 
			" and id_situacion=1 "+
			" and estatus='1'", nativeQuery=true)
	public List<TSituacionPersona> findImputados(@Param("idTsjdf") long idTsjdf);
	
	@Query (value= "select * " + 
			" from siemp_oral.t_situacion_persona " + 
			" where id_tsjdf= :idTsjdf" + 
			" and id_situacion=2 "+
			" and estatus='1'", nativeQuery=true)
	public List<TSituacionPersona> findVictimas(@Param("idTsjdf") long idTsjdf);
}
