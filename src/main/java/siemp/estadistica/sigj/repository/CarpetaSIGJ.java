package siemp.estadistica.sigj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import siemp.estadistica.entidades.TSigjCarpeta;
import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;

public interface CarpetaSIGJ extends JpaRepository<TSigjCarpeta, Long>{
	
	@Query (value= "select * from t_sigj_carpeta car " +
			"INNER JOIN t_ingreso i ON i.id_tsjdf=car.id_tsjdf "+
		    "AND i.estatus<>'0' "+
			"where car.id_carpeta= :idCarpeta " + 
			"order by car.id_tsjdf desc " + 
			"limit 1;", nativeQuery=true)
	TSigjCarpeta findByIdCarpeta(@Param("idCarpeta") Long idCarpeta);

}
