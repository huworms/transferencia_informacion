package siemp.estadistica.sigj.siemp;

import java.util.Calendar;
import java.util.Date;

import siemp.estadistica.entidades.CTipoAudiencia;
import siemp.estadistica.entidades.TAudienciaImputado;
import siemp.estadistica.sigj.repository.AudienciaRepository;

public class Audiencia {
	
	private AudienciaRepository audienciaRepository;
	
	public Audiencia(AudienciaRepository audienciaRepository) {
		this.audienciaRepository=audienciaRepository;
	}
	
	 public Long registrarAudiencia(Long idTsjdf, Date fechaAudiencia, Date fechaInicio,
			 Date fechaFin, int tipoAudiencia, int idJuez, String tipoRegistro) {
		 
		 TAudienciaImputado au=new TAudienciaImputado();
		 au.setIdTsjdf(idTsjdf);
		 au.setFechaAudiencia(fechaAudiencia);
		 au.setFechaConclusion(fechaFin);
		 au.setFechaInicio(fechaInicio);
		 au.setIdJuez(idJuez);
		 au.setIdTipoRegistro(tipoRegistro);
		 CTipoAudiencia tipo=new CTipoAudiencia();
		 tipo.setIdTipoAudiencia(tipoAudiencia);
		 au.setCTipoAudiencia(tipo);
		 au.setEstatus("1");
		 audienciaRepository.save(au);
		 return au.getIdEvento();
	 }
	 
	 public Long checkAudiencia(Long idTsjdf, Date fechaAudiencia,int tipoAudiencia) {
		 
		 Calendar cal=Calendar.getInstance();
		 cal.setTime(fechaAudiencia);
		 int mes=cal.get(Calendar.MONTH)+1;
		 int anio=cal.get(Calendar.YEAR);
		 
		 TAudienciaImputado ai=audienciaRepository.findAudiencia(tipoAudiencia, anio, mes, idTsjdf);
		 
		 return ai==null?null:ai.getIdEvento();
		 
	 }
	 
	 public TAudienciaImputado consultaAudiencia(long idEvento) {
		 /*TAudienciaImputado ai=audienciaRepository.findByIdEvento(idEvento);*/
		 TAudienciaImputado ai=audienciaRepository.getById(idEvento);
		 return ai;
	 }

}
