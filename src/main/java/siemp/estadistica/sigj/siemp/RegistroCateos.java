package siemp.estadistica.sigj.siemp;

import java.util.List;

import siemp.estadistica.entidades.CTecnicaInvestigacion;
import siemp.estadistica.entidades.TTecnicaInvestigacion;
import siemp.estadistica.entidades.TTsjdf;
import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;
import siemp.estadistica.sigj.intercambio_jpa.TPresuntoResponsable;
import siemp.estadistica.sigj.intercambio_jpa.TTVictima;
import siemp.estadistica.sigj.repository.TecnicaRepository;

public class RegistroCateos {
	
	public IngresoAsunto ingreso;
	public TecnicaRepository tecnicaRepository;
	
	public RegistroCateos(IngresoAsunto ingreso,
			TecnicaRepository tecnicaRepository)
	{
		this.ingreso=ingreso;
		this.tecnicaRepository=tecnicaRepository;
	}
	
	public TTsjdf registraCateo(TCarpeta carpeta, 
			 List<TPresuntoResponsable> imputados,
			 List<TTVictima> victimas, int idTipoAsunto, int idTecnica) {
		
		TTsjdf tsjdf=ingreso.guardarRadicacion(carpeta, imputados, victimas, idTipoAsunto,"0");
		TTecnicaInvestigacion tt=new TTecnicaInvestigacion();
		tt.setIdTsjdf(tsjdf.getIdTsjdf());
		CTecnicaInvestigacion tipo=new CTecnicaInvestigacion();
		tipo.setIdTecnica(idTecnica);
		tt.setCTecnicaInvestigacion(tipo);
		
		tecnicaRepository.save(tt);
		return tsjdf;
		
	}
	

}
