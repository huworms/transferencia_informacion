package siemp.estadistica.sigj.siemp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import siemp.estadistica.entidades.TIngreso;
import siemp.estadistica.sigj.TransferenciaUtil;
import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;
import siemp.estadistica.sigj.repository.CarpetasRepository;
import siemp.estadistica.sigj.repository.IngresoSIEMPRepository;
import siemp.estadistica.sigj.repository.PersonaRepository;
import siemp.estadistica.sigj.repository.SituacionRepository;

public class AntecedenteAsunto {
	
	@Autowired
	private IngresoSIEMPRepository siempRepository;
	@Autowired
	private PersonaRepository personaRepository;
	@Autowired
	private SituacionRepository situacionRepository;
	@Autowired
	private CarpetasRepository carpetaRepository;
	
	private static AntecedenteAsunto aa;
	
	private AntecedenteAsunto(CarpetasRepository carpetaRepository,
			IngresoSIEMPRepository siempRepository) {
		this.carpetaRepository=carpetaRepository;
		this.siempRepository=siempRepository;
		
	}
	
	public static AntecedenteAsunto getAntecedenteAsunto(CarpetasRepository carpetaRepository,
			IngresoSIEMPRepository siempRepository) {
		if(aa==null)
			aa=new AntecedenteAsunto(carpetaRepository, siempRepository);
		
		return aa;
			
	}
	
	public TCarpeta consultaCarpetaProcedencia(Long idCarpeta) {
		return carpetaRepository.findByIdCarpeta(idCarpeta);
		 
	}
	
	public TCarpeta consultaAntecedenteSIGJ(String numInvestigacion, int idEntidadActual) {
		List<TCarpeta> carpeta=carpetaRepository.consultarAntecedente(numInvestigacion, idEntidadActual);
		if(carpeta.isEmpty())
			return null;
		return carpeta.get(0);
		
	}
	
	
	
	public TIngreso consultaAntecedenteSIEMP(TCarpeta carpeta) {
		
		if(carpeta==null)
			return null;
		System.out.println("Carpeta Encontrada: "+carpeta.getCarpetaJudicial());
		TIngreso siemp=consultaCarpetaSIEMP(carpeta);
		if(siemp!=null)
			System.out.println("Carpeta Encontrada en SIEMP: "+siemp.getNumPartida());
		return siemp;
		
		
	}
	
	public TIngreso consultaCarpetaSIEMP(TCarpeta carpeta) {
		
		int idEntidad_aux=Integer.parseInt(carpeta.getIdUnidadGestion());
		int idEntidad=TransferenciaUtil.getIDEntidad(idEntidad_aux);
		List<TIngreso> lista=siempRepository.consultarCarpeta(carpeta.getAnioCarpeta(),
				carpeta.getNumCarpeta(), idEntidad);
		TIngreso ingresoSIEMP=null;
		if(carpeta.getCarpetaJudicial().contains("TE")) /* Juicio Oral*/
			ingresoSIEMP=consultaIngreso(lista,10);
		else
			ingresoSIEMP=consultaIngreso(lista, null);
		
		return ingresoSIEMP;
		
		
		
		
	}
	
	private TIngreso consultaIngreso(List<TIngreso> lista, Integer tipoIngreso)
	{
		TIngreso ingreso=null;
		if(tipoIngreso==null && lista.size()==1)
			ingreso=lista.get(0);
		else if(tipoIngreso==null && lista.size()>1)
			return null;
		else {
			for(TIngreso aux:lista) {
				if(aux.getCTipoAsunto().getIdTipoAsunto()==tipoIngreso.intValue()) {
					ingreso=aux;
					break;
				}
			}
		}
		return ingreso;
	}
	
	

	
	

}
