package siemp.estadistica.sigj.siemp;

import java.util.List;

import siemp.estadistica.entidades.CImpugnacionMp;
import siemp.estadistica.entidades.CTecnicaInvestigacion;
import siemp.estadistica.entidades.TImpugnacion;
import siemp.estadistica.entidades.TTecnicaInvestigacion;
import siemp.estadistica.entidades.TTsjdf;
import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;
import siemp.estadistica.sigj.intercambio_jpa.TPresuntoResponsable;
import siemp.estadistica.sigj.intercambio_jpa.TTVictima;
import siemp.estadistica.sigj.repository.ImpugnacionRepository;
import siemp.estadistica.sigj.repository.TecnicaRepository;

public class RegistroImpugnacion {
	
	public IngresoAsunto ingreso;
	public ImpugnacionRepository impugnaRepository;
	
	
	public RegistroImpugnacion(IngresoAsunto ingreso,
			ImpugnacionRepository impugnaRepository)
	{
		this.ingreso=ingreso;
		this.impugnaRepository=impugnaRepository;
	}
	
	
	public TTsjdf registraImpugnacion(TCarpeta carpeta, 
			 List<TPresuntoResponsable> imputados,
			 List<TTVictima> victimas, int idTipoAsunto, int idImpugnacion) {
		
		TTsjdf tsjdf=ingreso.guardarRadicacion(carpeta, imputados, victimas, idTipoAsunto,"0");
		
		TImpugnacion impugna=new TImpugnacion();
		impugna.setIdTsjdf(tsjdf.getIdTsjdf());
		CImpugnacionMp im=new CImpugnacionMp();
		im.setIdTipo(idImpugnacion);
		
		impugna.setCImpugnacionMp(im);
		impugna.setOtroTipo("No Especificado");
		
		impugnaRepository.save(impugna);
		
		return tsjdf;
		
	}
	

}
