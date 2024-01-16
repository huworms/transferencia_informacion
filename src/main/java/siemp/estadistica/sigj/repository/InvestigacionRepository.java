package siemp.estadistica.sigj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import siemp.estadistica.entidades.TInvestigacionMp;

@Repository
public interface InvestigacionRepository extends JpaRepository<TInvestigacionMp, Long>{

}
