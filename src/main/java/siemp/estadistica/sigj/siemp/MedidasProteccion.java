package siemp.estadistica.sigj.siemp;

import java.util.Date;
import java.util.List;

import siemp.estadistica.entidades.CTipoActo;
import siemp.estadistica.entidades.TActoProcesal;
import siemp.estadistica.entidades.TAudienciaImputado;
import siemp.estadistica.entidades.TMedidaVictima;
import siemp.estadistica.entidades.TSigjCarpeta;
import siemp.estadistica.entidades.TSigjPersona;
import siemp.estadistica.entidades.TTsjdf;
import siemp.estadistica.sigj.TransferenciaUtil;
import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;
import siemp.estadistica.sigj.intercambio_jpa.TMedidaCnpp;
import siemp.estadistica.sigj.intercambio_jpa.TMedidaLamvlv;
import siemp.estadistica.sigj.intercambio_jpa.TTVictima;
import siemp.estadistica.sigj.repository.ActoProcesalRepository;
import siemp.estadistica.sigj.repository.CarpetaSIGJ;
import siemp.estadistica.sigj.repository.MedidaProteccionCNPP;
import siemp.estadistica.sigj.repository.MedidaProteccionSIGJ;
import siemp.estadistica.sigj.repository.PersonaSIGJ;
import siemp.estadistica.sigj.repository.TMedidaVictimaRepository;
import siemp.estadistica.sigj.repository.VictimaRepository;

public class MedidasProteccion {
	
	public Audiencia audiencia;
	private MedidaProteccionSIGJ medidasSIGJ;
	private MedidaProteccionCNPP medidasCnpp;
	private CarpetaSIGJ carpetaSIGJ;
	private PersonaSIGJ personaSIGJ;
	private TMedidaVictimaRepository medidaVictimaRepository;
	private VictimaRepository victimaRepository;
	private ActoProcesalRepository actoProcesalRepository;
	
	private static String MEDIDA_CONCEDIDA="1";
	private static int ID_TIPO_ASUNTO=3;
	private static int ID_TIPO_AUDIENCIA=9;
	private static int ID_ACTO=9;
	
	public MedidasProteccion(Audiencia audiencia, MedidaProteccionSIGJ medidasSIGJ,
			CarpetaSIGJ carpetaSIGJ, PersonaSIGJ personaSIGJ, TMedidaVictimaRepository medidaVictimaRepository,
			VictimaRepository victimaRepository, ActoProcesalRepository actoProcesalRepository,
			MedidaProteccionCNPP medidasCnpp) {
		this.audiencia=audiencia;
		this.medidasSIGJ=medidasSIGJ;
		this.carpetaSIGJ=carpetaSIGJ;
		this.personaSIGJ=personaSIGJ;
		this.medidaVictimaRepository=medidaVictimaRepository;
		this.victimaRepository=victimaRepository;
		this.actoProcesalRepository=actoProcesalRepository;
		this.medidasCnpp=medidasCnpp;
	}
	
	
	public void actualizarResolucionMedidasLAMVLV(List<TCarpeta> listaSIGJ) {
		
		for(TCarpeta carpeta: listaSIGJ) {
			/*if(carpeta.getIdUnidadGestion().equals("006") && carpeta.getNumCarpeta()==786)
			{
				List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
				this.actualizarResolucionMedidas(victimas);
			}*/
			List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
			this.actualizarResolucionMedidas(victimas);
		}
		
	/*	List<TTVictima> victimas=consultaVictimas(listaSIGJ.get(0).getIdCarpeta());
		this.actualizarResolucionMedidas(victimas);*/
	}
	
	
	public void actualizarMedidasCNPP(List<TCarpeta> listaSIGJ) {
		
		for(TCarpeta carpeta: listaSIGJ) {
			/*if(carpeta.getIdUnidadGestion().equals("002") && carpeta.getNumCarpeta()==564)
			{
				List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
				this.actualizarResolucionMedidasCNPP(victimas);
			}*/
			List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
			this.actualizarResolucionMedidasCNPP(victimas);
		}
		
	}
	
	public List<TTVictima> consultaVictimas(Long idCarpeta)
	{
		return victimaRepository.findByIdCarpeta(idCarpeta);
	}
	
	private void actualizarResolucionMedidas(List<TTVictima> victimas) {
		
		int tipo_medida;
		List<TMedidaVictima> lista;
		boolean agregarActo=false;
		Date fechaResolucion=null;
		TSigjCarpeta sigj;
		Long idEvento=null;
		
		for(TTVictima vic: victimas) {
			List<TMedidaLamvlv> medidas=medidasSIGJ.findByIdPersona(vic.getIdPersona());
			sigj=carpetaSIGJ.findByIdCarpeta(vic.getIdCarpeta());
			if(sigj==null) {
				System.out.println("NO SE ENCONTRO CARPETA: "+vic.getIdCarpeta());
				return;
			}
			
			TSigjPersona persona_tmp=personaSIGJ.findByIdPersonaSigj(vic.getIdPersona(), sigj.getIdTsjdf());
			if(persona_tmp==null || medidas.isEmpty()) {
				System.out.println("NO SE ENCONTRO CARPETA Y PERSONA: "+vic.getIdPersona()+", TSJDF: "+sigj.getIdTsjdf());
				return;
			}
			
			
			for(TMedidaLamvlv aux: medidas) {
				if(aux.getResolucionMedida()!=null && 
						!aux.getResolucionMedida().equals("")) {
					tipo_medida=TransferenciaUtil.getNumFromRoma(aux.getTipoMedida());
					tipo_medida=tipo_medida+24;
					lista=medidaVictimaRepository.
								consultarRegistroMedida(sigj.getIdTsjdf(), persona_tmp.getIdPersona(), tipo_medida);
					
					System.out.println("ACTUALIZANDO RESOLUCIONES: ID_TSJDF="
							+sigj.getIdTsjdf()+", ID_PERSONA: "+persona_tmp.getIdPersona()+", TIPO_MEDIDA: "+tipo_medida);
					agregarActo=true;
					fechaResolucion=aux.getFechaResolucion();
					actualizaResolucion(lista, aux.getResolucionMedida());
					
				}
			}
			if(agregarActo) {
					idEvento=(idEvento==null)?checkAddAudiencia(sigj.getIdTsjdf(),fechaResolucion ):idEvento;
					agregarActo(idEvento, persona_tmp.getIdPersona());
			}
				
		}
		
		
		
	}
	
	
private void actualizarResolucionMedidasCNPP(List<TTVictima> victimas) {
		
		Date fechaResolucion=null;
		TSigjCarpeta sigj;
		Long idEvento=null;
		
		for(TTVictima vic: victimas) {
			
			sigj=carpetaSIGJ.findByIdCarpeta(vic.getIdCarpeta());
			if(sigj==null || sigj.getIdTsjdf()==null)
				return;
			 TSigjPersona persona_tmp=personaSIGJ.findByIdPersonaSigj(vic.getIdPersona(), sigj.getIdTsjdf());
			 if(persona_tmp==null)
				 return;
			
			fechaResolucion=registrar_medidas_proteccionCNPP(vic.getIdPersona(), sigj.getIdTsjdf());
			if(fechaResolucion!=null)
			{
				idEvento=checkAddAudiencia(sigj.getIdTsjdf(),fechaResolucion);
				agregarActo(idEvento, persona_tmp.getIdPersona());
			}
				
		}
}
		
	
	
		
	private void agregarActo(Long idEvento,Long idPersona) {
		TActoProcesal acto;
		
		acto=actoProcesalRepository.findActoProcesal(MedidasProteccion.ID_ACTO, idEvento, idPersona);
		if(acto!=null)
			return; 
		acto=new TActoProcesal();
		TAudienciaImputado ai=audiencia.consultaAudiencia(idEvento);
		acto.setTAudienciaImputado(ai);
		acto.setIdPersona(idPersona);
		CTipoActo tipo_acto=new CTipoActo();
		tipo_acto.setIdTipoActo(MedidasProteccion.ID_ACTO);
		acto.setCTipoActo(tipo_acto);
		acto.setEstatus("1");
		actoProcesalRepository.save(acto);
		
		
		
	}
	
	private void actualizaResolucion(List<TMedidaVictima> lista, String resolucion) {
		for(TMedidaVictima vic: lista) {
			vic.setOtorgamiento(resolucion);
			medidaVictimaRepository.save(vic);
		}
	}
	
	private void actualizaResolucion(List<TMedidaVictima> lista, String resolucion, int tipoMedida, Long idPersona, Long idTsjdf) {
		if(lista==null || lista.isEmpty()) {
			
		}else {
		
			for(TMedidaVictima vic: lista) {
				vic.setOtorgamiento(resolucion);
				medidaVictimaRepository.save(vic);
			}
		}
	}
	
	
	private Date registrar_medidas_proteccionCNPP(Long idPersonaSigj, Long idTsjdf) {
		 
		 List<TMedidaVictima> lista;
		 List<TMedidaCnpp> medidas=medidasCnpp.findByIdPersona(idPersonaSigj);
		 if(medidas.isEmpty())
			 return null;
		 TSigjPersona persona_tmp=personaSIGJ.findByIdPersonaSigj(idPersonaSigj, idTsjdf);
		 if(persona_tmp==null)
			 return null;
		 
		 System.out.println("ID TSJDF: "+idTsjdf+ ", "+
				 "ID_VICTIMA_SIEMP: "+persona_tmp.getIdPersona());
		 int tipo_medida=0;
		 TMedidaVictima mv=null;
		 Date fechaResolucion=null;
		 for(TMedidaCnpp aux: medidas) {
			 if(aux.getTipoMedida().equals(""))
				 continue;
			 tipo_medida=TransferenciaUtil.getNumFromRoma(aux.getTipoMedida());
			 
			 lista=medidaVictimaRepository.
				consultarRegistroMedida(idTsjdf, persona_tmp.getIdPersona(), tipo_medida);
			 if(lista==null || lista.isEmpty())
				 guardarDatosMedida(persona_tmp.getIdPersona(), idTsjdf, tipo_medida, 
						 aux.getResolucionMedida(), aux.getFechaResolucion());
			 else
				 actualizaResolucion(lista, aux.getResolucionMedida());
			 
			 fechaResolucion=aux.getFechaResolucion();
		 }
		 
		
		 
		 return fechaResolucion;
	 }
	
	
	private void guardarDatosMedida(Long idPersona, Long idTsjdf, int tipoMedida, 
			String otorgamiento, Date fechaResolucion) {
		
		 TMedidaVictima mv=new TMedidaVictima();
		 mv.setIdPersona(idPersona);
		 mv.setIdTsjdf(idTsjdf);
		 mv.setIdTipoMedida(tipoMedida);
		 mv.setFechaSolicitudMedida(null);
		 mv.setOtorgamiento(otorgamiento);
		 mv.setEstatus("1");
		 medidaVictimaRepository.save(mv);
		 
		
	}
	
	private long checkAddAudiencia(Long idTsjdf, Date fechaAudiencia) {
		Long idEvento=audiencia.checkAudiencia(idTsjdf, fechaAudiencia,
				MedidasProteccion.ID_TIPO_AUDIENCIA);
		if(idEvento!=null)
			return idEvento;
		
		idEvento=audiencia.registrarAudiencia(idTsjdf, fechaAudiencia, fechaAudiencia,
				fechaAudiencia,MedidasProteccion.ID_TIPO_AUDIENCIA , -1, "2");
		return idEvento;
	}
	
	
	

}
