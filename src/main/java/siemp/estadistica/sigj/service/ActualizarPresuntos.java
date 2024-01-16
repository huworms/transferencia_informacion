package siemp.estadistica.sigj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import siemp.estadistica.entidades.TSigjCarpeta;
import siemp.estadistica.entidades.TSigjPersona;
import siemp.estadistica.entidades.TSituacionPersona;
import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;
import siemp.estadistica.sigj.intercambio_jpa.TPresuntoResponsable;
import siemp.estadistica.sigj.repository.CarpetaSIGJ;
import siemp.estadistica.sigj.repository.IngresoSIEMPRepository;
import siemp.estadistica.sigj.repository.PersonaRepository;
import siemp.estadistica.sigj.repository.PersonaSIGJ;
import siemp.estadistica.sigj.repository.SituacionRepository;
import siemp.estadistica.sigj.siemp.IngresoAsunto;

public class ActualizarPresuntos {
	
	
	private CarpetaSIGJ carpetaSIGJ;
	private PersonaSIGJ personaSIGJ;
	private SituacionRepository situacionRepository;
	private IngresoAsunto ingreso;
	
	public ActualizarPresuntos(CarpetaSIGJ carpetaSIGJ,PersonaSIGJ personaSIGJ,
			SituacionRepository situacionRepository, IngresoAsunto ingreso)
	{
		this.carpetaSIGJ=carpetaSIGJ;
		this.personaSIGJ=personaSIGJ;
		this.situacionRepository=situacionRepository;
		this.ingreso=ingreso;
	}
	
	
	
	public void actualizarImputados(TCarpeta carpeta, List<TPresuntoResponsable> imputados) {
		
		/* revisar que la carpeta haya sido cargada */
		TSigjCarpeta car=carpetaSIGJ.findByIdCarpeta(carpeta.getIdCarpeta());
		if(car==null)
			return;
		
		/* Checar que hay mas imputados que los registrados */
		
		List<TSituacionPersona> registros=situacionRepository.findImputados(car.getIdTsjdf());
		
		if(registros.size()==imputados.size())
			return;
		
		/* checar cuales imputados faltan por cargar*/
		
		TSigjPersona aux=null;
		
		for(TPresuntoResponsable pr: imputados) {
			aux=personaSIGJ.findByIdPersonaPre(pr.getIdPersona(), pr.getIdCarpeta(), car.getIdTsjdf());
			if(aux==null){
				System.out.println("ACTUALIZANDO : -----"+pr.getIdPersona());
				agregarPersona(car.getIdTsjdf(),pr);
			}
					
		}
		
	}
	
	

	public void agregarPersona(Long idTsjdf, TPresuntoResponsable pr) {
		ingreso.guardarImputado(pr, idTsjdf, "1");
	}
	
	
	

}
