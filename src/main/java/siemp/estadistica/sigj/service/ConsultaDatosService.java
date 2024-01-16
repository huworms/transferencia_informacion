package siemp.estadistica.sigj.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import siemp.estadistica.entidades.TIngreso;
import siemp.estadistica.sigj.TransferenciaUtil;
import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;
import siemp.estadistica.sigj.intercambio_jpa.TPresuntoResponsable;
import siemp.estadistica.sigj.intercambio_jpa.TTVictima;
import siemp.estadistica.sigj.repository.CarpetaSIGJ;
import siemp.estadistica.sigj.repository.CarpetasRepository;
import siemp.estadistica.sigj.repository.IngresoSIEMPRepository;
import siemp.estadistica.sigj.repository.PresuntoRepository;
import siemp.estadistica.sigj.repository.VictimaRepository;
import siemp.estadistica.sigj.siemp.AntecedenteAsunto;
import siemp.estadistica.sigj.siemp.IngresoAsunto;
import siemp.estadistica.sigj.siemp.RegistroCateos;
import siemp.estadistica.sigj.siemp.RegistroImpugnacion;

@Service
public class ConsultaDatosService {
	
	@Autowired
	private CarpetasRepository carpetaRepository;
	private IngresoSIEMPRepository siempRepository;
	private PresuntoRepository presuntoRepository;
	private VictimaRepository victimaRepository;
	
	public ConsultaDatosService(CarpetasRepository carpetaRepository,
			IngresoSIEMPRepository siempRepository,
			PresuntoRepository presuntoRepository,
			VictimaRepository victimaRepository)
	{
		this.carpetaRepository=carpetaRepository;
		this.siempRepository=siempRepository;
		this.presuntoRepository=presuntoRepository;
		this.victimaRepository=victimaRepository;
	}
	
	public List<TCarpeta> consultaCarpetas(){
		System.out.println("Iniciando consulta de carpetas");
		List<TCarpeta> lista=carpetaRepository.findAll();
		for(TCarpeta a: lista) {
			System.out.println("ID: "+a.getIdCarpeta()+", CARPETA="+a.getCarpetaJudicial());
		}
		return lista;
		
	}
	
	
	public List<TCarpeta> consultaCarpetasByTipo (int idTipoAudiencia, int anio, int mes){
		System.out.println("Iniciando consulta de carpetas");
		/*List<TCarpeta> lista=carpetaRepository.findByIdTipoAudiencia(idTipoAudiencia);*/
		List<TCarpeta> lista=carpetaRepository.consultarCarpetas(anio, mes,idTipoAudiencia);
		System.out.println("Numero de registros encontrados: "+lista.size());
		/*for(TCarpeta a: lista) {
			System.out.println("ID: "+a.getIdCarpeta()+", CARPETA="+a.getCarpetaJudicial());
		}*/
		return lista;
		
	}
	
	public List<TCarpeta> consultaCarpetas (int anio, int mes){
		System.out.println("Iniciando consulta de carpetas");
		/*List<TCarpeta> lista=carpetaRepository.findByIdTipoAudiencia(idTipoAudiencia);*/
		List<TCarpeta> lista=carpetaRepository.consultarCarpetas(anio, mes);
		System.out.println("Numero de registros encontrados: "+lista.size());
		/*for(TCarpeta a: lista) {
			System.out.println("ID: "+a.getIdCarpeta()+", CARPETA="+a.getCarpetaJudicial());
		}*/
		return lista;
		
	}
	
	public void actualizarImputados(List<TCarpeta> listaSIGJ, ActualizarPresuntos ap) {
		for(TCarpeta car: listaSIGJ) {
			if((Integer.parseInt(car.getIdUnidadGestion())<12))
			{
				List<TPresuntoResponsable> imputados=consultaImputados(car.getIdCarpeta());
				ap.actualizarImputados(car, imputados);
			}
		}
	}
	
	public boolean checkCarpetaSIEMP(int numCarpeta, int anioCarpeta,
			int idEntidadAux, int idTipoAsunto) {
		
		int idEntidad= TransferenciaUtil.getIDEntidad(idEntidadAux);
		List<Object[]>lista= siempRepository.
				consultarExisteCarpeta(anioCarpeta, numCarpeta, idEntidad, idTipoAsunto);
		return !lista.isEmpty();
		
	}
	
	public void iniciarIngresoCarpeta(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto,
			String consignacion) {
		int idTipoAsunto=1;
		
	
		for(TCarpeta carpeta: listaSIGJ) {
			if(!(Integer.parseInt(carpeta.getIdUnidadGestion())<17 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==301)) // Ingresar solo carpetas unidad 1 
				continue;
		/*	if(carpeta.getFechaSolicitud().before(inicio.getTime()))
				continue;
				*/
			if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
					Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
			{
			/*	if(carpeta.getNumCarpeta()==2)
				{*/
					System.out.println("Iniciando proceso de insert de datos.....");
					System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
					List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
					List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
					if(victimas.isEmpty())
						victimas=victimaNoEspecificad();
					
					ingresoAsunto.guardarRadicacion(carpeta, imputados, victimas, idTipoAsunto, consignacion);
					
			/*	}*/
			}
			
				
		}
	}
	
	public void ingresoEjecucionFixed(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto) {
		
		int idTipoAsunto=7;
		AntecedenteAsunto aa=AntecedenteAsunto.getAntecedenteAsunto(carpetaRepository, 
				siempRepository);
		Calendar fecha_inicio=Calendar.getInstance();
		fecha_inicio.set(Calendar.YEAR, 2022);
		fecha_inicio.set(Calendar.MONTH, Calendar.MARCH);
		fecha_inicio.set(Calendar.DAY_OF_MONTH,1);
		
		for(TCarpeta carpeta: listaSIGJ) {
			if(carpeta.getCarpetaJudicial().contains("LN"))
				continue;
			
			if(!(Integer.parseInt(carpeta.getIdUnidadGestion())==309 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==501 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==601 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==604 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==605 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==302
						)) // Ingresar solo carpetas de ejecucion
				continue;
			
		/*	if(!carpeta.getIdCarpeta().equals(1662069316923l))
				continue;*/
			if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
					Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto)){
				
				System.out.println("Iniciando proceso de insert de datos.....");
				System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
				List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
				List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
				if(victimas.isEmpty())
					victimas=victimaNoEspecificad();
				
				if(carpeta.getProcedencia()!=null){
					TCarpeta carpetaProc =aa.consultaCarpetaProcedencia(Long.parseLong(carpeta.getProcedencia()));
					TIngreso ingreso=aa.consultaAntecedenteSIEMP(carpetaProc);
					
					if(ingreso!=null)
						System.out.print("Fecha solicitud ingreso procedencia: "+ingreso.getFechaSolicitudAudiencia());
					
					if (carpetaProc!=null && ingreso!=null)
						/*if(ingreso.getFechaSolicitudAudiencia().before(fecha_inicio.getTime()))
							continue;
						else*/ 
							ingresoAsunto.guardarRadicacionProcedencia(carpeta, ingreso,idTipoAsunto, imputados, victimas);
					
				}
			}
		}
		
		
	}
	
	
	public void iniciarIngresoEjecucion(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto) {
		
		int idTipoAsunto=7;
		
		AntecedenteAsunto aa=AntecedenteAsunto.getAntecedenteAsunto(carpetaRepository, 
				siempRepository);
		for(TCarpeta carpeta: listaSIGJ) {
			if(carpeta.getCarpetaJudicial().contains("LN"))
				continue;
			if(!(Integer.parseInt(carpeta.getIdUnidadGestion())==309 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==501 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==601 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==604 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==605 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==302
						)) // Ingresar solo carpetas de ejecucion
				continue;
			
		/*	if(!(carpeta.getNumCarpeta()==275 && Integer.parseInt(carpeta.getIdUnidadGestion())==309))
				continue;*/
				
			if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
					Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
			{
					System.out.println("Iniciando proceso de insert de datos.....");
					System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
					List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
					List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
					if(victimas.isEmpty())
						victimas=victimaNoEspecificad();
					
					if(carpeta.getProcedencia()!=null)
					{
						/*Long antecedente=Long.parseLong(carpeta.getProcedencia());*/
						TCarpeta carpetaProc =aa.consultaCarpetaProcedencia(Long.parseLong(carpeta.getProcedencia()));
						TCarpeta carpetaSIGJ= aa.consultaAntecedenteSIGJ(carpeta.getNumInvestigacion(),
								Integer.parseInt(carpeta.getIdUnidadGestion()));
						TIngreso ingreso=aa.consultaAntecedenteSIEMP(carpetaProc);
						TIngreso ingresoSIGJ=aa.consultaAntecedenteSIEMP(carpetaSIGJ);
						
						if (carpetaProc!=null && ingreso!=null)
							ingresoAsunto.guardarRadicacionAntecedenteSIEMP(carpeta, ingreso,idTipoAsunto);
						else if(carpetaSIGJ!=null && ingresoSIGJ!=null)
							ingresoAsunto.guardarRadicacionAntecedenteSIEMP(carpeta, ingresoSIGJ,idTipoAsunto);
						else if(carpetaProc!=null)
							ingresoAsunto.guardarRadicacionAntecedenteSIGJ(carpeta, imputados, victimas, idTipoAsunto, carpetaProc);
						else if(carpetaSIGJ!=null)
							ingresoAsunto.guardarRadicacionAntecedenteSIGJ(carpeta, imputados, victimas, idTipoAsunto, carpetaSIGJ);
						else {
							carpetaSIGJ=new TCarpeta();
							carpetaSIGJ.setAnioCarpeta(2022);
							carpetaSIGJ.setNumCarpeta(999);
							carpetaSIGJ.setNumInvestigacion("");
							carpetaSIGJ.setIdUnidadGestion("1");
							ingresoAsunto.guardarRadicacionAntecedenteSIGJ(carpeta, imputados, victimas, idTipoAsunto, carpetaSIGJ);
						}
							
					}
					/*else
						ingresoAsunto.guardarRadicacion(carpeta, imputados, victimas,7);*/
					
					
					
				
			}
			
				
		}
	}
	
	
	public void ingresoMedidaProteccion(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto) {
		int idTipoAsunto=3;
		
		for(TCarpeta carpeta: listaSIGJ) {
			if(!(Integer.parseInt(carpeta.getIdUnidadGestion())<17
					)) // Ingresar solo carpetas de control
				continue;
			if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
					Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
			{
				System.out.println("Iniciando proceso de insert de medidas de proteccion");
				System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
				List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
				List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
				if(victimas.isEmpty())
					victimas=victimaNoEspecificad();
				
				ingresoAsunto.guardarMedidaProteccionLAMVLV(carpeta, 
						imputados, victimas,idTipoAsunto);
				
			}
			
		}
	}
	
	
	public void ingresoMedidaProteccionCNPP(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto) {
		int idTipoAsunto=3;
		
		for(TCarpeta carpeta: listaSIGJ) {
			if(!(Integer.parseInt(carpeta.getIdUnidadGestion())<17
					)) // Ingresar solo carpetas de control
				continue;
			if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
					Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
			{
				System.out.println("Iniciando proceso de insert de medidas de proteccion CNPP");
				System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
				List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
				List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
				if(victimas.isEmpty())
					victimas=victimaNoEspecificad();
				
				ingresoAsunto.guardarMedidaProteccionCNPP(carpeta, 
						imputados, victimas,idTipoAsunto);
				
			}
			
		}
	}
	
	public void ingresoCateo(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto, 
			RegistroCateos cateo, int idTecnica) {
		int idTipoAsunto=12;
		
		for(TCarpeta carpeta: listaSIGJ) {
			if(!(Integer.parseInt(carpeta.getIdUnidadGestion())<17
					)) // Ingresar solo carpetas de control
				continue;
			if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
					Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
			{
				System.out.println("Iniciando proceso de insert de cateo");
				System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
				List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
				List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
				
				cateo.registraCateo(carpeta, imputados, victimas, idTipoAsunto , idTecnica);
			}
			
		}
	}
	
	
public void iniciarIngresoIncompetencia(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto) {
		
		int idTipoAsunto=2;
		
		AntecedenteAsunto aa=AntecedenteAsunto.getAntecedenteAsunto(carpetaRepository, 
				siempRepository);
		for(TCarpeta carpeta: listaSIGJ) {
			if(carpeta.getCarpetaJudicial().contains("TE"))
				continue;
			if(!(Integer.parseInt(carpeta.getIdUnidadGestion())<17 ||
					Integer.parseInt(carpeta.getIdUnidadGestion())==301)) // Ingresar solo carpetas de control
				continue;
			
			/*if(!(carpeta.getNumCarpeta()==161))
				continue;
				*/
			if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
					Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
			{
					System.out.println("Iniciando proceso de insert de datos.....");
					System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
					List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
					List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
					if(victimas.isEmpty())
						victimas=victimaNoEspecificad();
					
					if(carpeta.getProcedencia()!=null && !carpeta.getProcedencia().isBlank())
					{
						/*Long antecedente=Long.parseLong(carpeta.getProcedencia());*/
						
						TCarpeta carpetaProc =aa.consultaCarpetaProcedencia(Long.parseLong(carpeta.getProcedencia()));
						TCarpeta carpetaSIGJ= aa.consultaAntecedenteSIGJ(carpeta.getNumInvestigacion(),
								Integer.parseInt(carpeta.getIdUnidadGestion()));
						TIngreso ingreso=aa.consultaAntecedenteSIEMP(carpetaSIGJ);
						/*if (carpetaProc!=null && ingreso!=null)
							ingresoAsunto.guardarRadicacionAntecedenteSIEMP(carpeta, ingreso,idTipoAsunto);
						else if(carpetaSIGJ!=null && ingreso!=null)
							ingresoAsunto.guardarRadicacionAntecedenteSIEMP(carpeta, ingreso,idTipoAsunto);
						else if(carpetaProc!=null)
							ingresoAsunto.guardarRadicacionAntecedenteSIGJ(carpeta, imputados, victimas, idTipoAsunto, carpetaProc);
						else if(carpetaSIGJ!=null)
							ingresoAsunto.guardarRadicacionAntecedenteSIGJ(carpeta, imputados, victimas, idTipoAsunto, carpetaSIGJ);
						else {
							carpetaSIGJ=new TCarpeta();
							carpetaSIGJ.setAnioCarpeta(2022);
							carpetaSIGJ.setNumCarpeta(999);
							carpetaSIGJ.setNumInvestigacion("");
							carpetaSIGJ.setIdUnidadGestion("16");
							ingresoAsunto.guardarRadicacionAntecedenteSIGJ(carpeta, imputados, victimas, idTipoAsunto, carpetaSIGJ);
						}*/
						if(carpetaProc!=null)
							ingresoAsunto.guardarRadicacionAntecedenteSIGJ(carpeta, imputados, victimas, idTipoAsunto, carpetaProc);
						else if(carpetaSIGJ!=null && ingreso!=null)
							ingresoAsunto.guardarRadicacionAntecedenteSIEMP(carpeta, ingreso,idTipoAsunto);
						else {
							carpetaSIGJ=new TCarpeta();
							carpetaSIGJ.setAnioCarpeta(2022);
							carpetaSIGJ.setNumCarpeta(999);
							carpetaSIGJ.setNumInvestigacion("");
							carpetaSIGJ.setIdUnidadGestion("16");
							ingresoAsunto.guardarRadicacionAntecedenteSIGJ(carpeta, imputados, victimas, idTipoAsunto, carpetaSIGJ);
						}
							
							
					}
					/*else
						ingresoAsunto.guardarRadicacion(carpeta, imputados, victimas,7);*/

			}
					
		}
	}


public void iniciarIngresoTE(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto) {
	
	int idTipoAsunto=10;
	
	AntecedenteAsunto aa=AntecedenteAsunto.getAntecedenteAsunto(carpetaRepository, 
			siempRepository);
	for(TCarpeta carpeta: listaSIGJ) {
		if(!carpeta.getCarpetaJudicial().contains("TE"))
			continue;
		if(!(Integer.parseInt(carpeta.getIdUnidadGestion())<17 || 
				Integer.parseInt(carpeta.getIdUnidadGestion())==301)) // Ingresar solo carpetas de control
			continue;
		
		/*if(!(carpeta.getNumCarpeta()==290))
			continue;*/
			
		if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
				Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
		{
				System.out.println("Iniciando proceso de insert de datos.....");
				System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
				List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
				List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
				if(victimas.isEmpty())
					victimas=victimaNoEspecificad();
				
				if(carpeta.getProcedencia()!=null && !carpeta.getProcedencia().isEmpty())
				{
					/*Long antecedente=Long.parseLong(carpeta.getProcedencia());*/
					
					TCarpeta carpetaProc =aa.consultaCarpetaProcedencia(Long.parseLong(carpeta.getProcedencia()));
					TCarpeta carpetaSIGJ= aa.consultaAntecedenteSIGJ(carpeta.getNumInvestigacion(),
							Integer.parseInt(carpeta.getIdUnidadGestion()));
					TIngreso ingreso=aa.consultaAntecedenteSIEMP(carpetaProc);
					TIngreso ingresoSIGJ=aa.consultaAntecedenteSIEMP(carpetaSIGJ);
					if (carpetaProc!=null && ingreso!=null)
						ingresoAsunto.guardarRadicacionAntecedenteSIEMP(carpeta, ingreso,idTipoAsunto);
					else if(carpetaSIGJ!=null && ingresoSIGJ!=null)
						ingresoAsunto.guardarRadicacionAntecedenteSIEMP(carpeta, ingresoSIGJ,idTipoAsunto);
					else if(carpetaProc!=null)
						ingresoAsunto.guardarRadicacionAntecedenteSIGJ(carpeta, imputados, victimas, idTipoAsunto, carpetaProc);
					else if(carpetaSIGJ!=null)
						ingresoAsunto.guardarRadicacionAntecedenteSIGJ(carpeta, imputados, victimas, idTipoAsunto, carpetaSIGJ);
					else
						ingresoAsunto.guardarRadicacion(carpeta, imputados, victimas,10,"0");
				}
				else
					ingresoAsunto.guardarRadicacion(carpeta, imputados, victimas,10,"0");

		}
				
	}
}

	

public void ingresoImpugnacion(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto, 
		RegistroImpugnacion impugnacion) {
	int idTipoAsunto=8;
	
	for(TCarpeta carpeta: listaSIGJ) {
		if(!(Integer.parseInt(carpeta.getIdUnidadGestion())<17
				)) // Ingresar solo carpetas de control
			continue;
		if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
				Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
		{
			System.out.println("Iniciando proceso de insert de cateo");
			System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
			List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
			List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
			
			impugnacion.registraImpugnacion(carpeta, imputados, victimas, idTipoAsunto, 99);
		}
		
	}
}

public void iniciarIngresoOtros(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto,
		String consignacion) {
	int idTipoAsunto=99;
	

	for(TCarpeta carpeta: listaSIGJ) {
		if(carpeta.getCarpetaJudicial().contains("-") && (Integer.parseInt(carpeta.getIdUnidadGestion())<17 ||
				Integer.parseInt(carpeta.getIdUnidadGestion())==301))
			continue;
		if(carpeta.getCarpetaJudicial().contains("LN"))
			continue;
		if(carpeta.getCarpetaJudicial().contains("EX"))
			continue;
		
		
		if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
				Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
		{
				System.out.println("Iniciando proceso de insert de datos otros tipos de audiencia.....");
				System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
				List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
				List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
				if(victimas.isEmpty())
					victimas=victimaNoEspecificad();
				
				ingresoAsunto.guardarRadicacion(carpeta, imputados, victimas, idTipoAsunto, consignacion);
				
		}
		
			
	}
}


public void iniciarIngresoOtrosuga12(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto,
		String consignacion) {
	int idTipoAsunto=99;
	

	for(TCarpeta carpeta: listaSIGJ) {
		if(Integer.parseInt(carpeta.getIdUnidadGestion())!=12)
			continue;
		if(!(carpeta.getCarpetaJudicial().contains("OC") 
				|| carpeta.getCarpetaJudicial().contains("OA")
				|| carpeta.getCarpetaJudicial().contains("AI")))
			continue;
		if(!carpeta.getRemision().isBlank())
			continue;
		
		
		if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
				Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
		{
				System.out.println("Iniciando proceso de insert de datos otros tipos de audiencia.....");
				System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
				List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
				List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
				if(victimas.isEmpty())
					victimas=victimaNoEspecificad();
				
				ingresoAsunto.guardarRadicacion(carpeta, imputados, victimas, idTipoAsunto, consignacion);
				
		}
		
			
	}
	
}




public void iniciarIngresoAccionParticular(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto,
		String consignacion) {
	int idTipoAsunto=9;
	

	for(TCarpeta carpeta: listaSIGJ) {
		if(carpeta.getCarpetaJudicial().contains("-") && (Integer.parseInt(carpeta.getIdUnidadGestion())<17 ||
				Integer.parseInt(carpeta.getIdUnidadGestion())==301))
			continue;
		if(carpeta.getCarpetaJudicial().contains("LN"))
			continue;
		if(carpeta.getCarpetaJudicial().contains("EX"))
			continue;
		
		
		if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
				Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto))
		{
				System.out.println("Iniciando proceso de insert de datos otros tipos de audiencia.....");
				System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
				List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
				List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
				if(victimas.isEmpty())
					victimas=victimaNoEspecificad();
				
				ingresoAsunto.guardarRadicacion(carpeta, imputados, victimas, idTipoAsunto, consignacion);
				
		}
		
			
	}
}



public void ingresoEjecucionIPreventivo(List<TCarpeta> listaSIGJ, IngresoAsunto ingresoAsunto) {
	
	int idTipoAsunto=15;
	AntecedenteAsunto aa=AntecedenteAsunto.getAntecedenteAsunto(carpetaRepository, 
			siempRepository);
	Calendar fecha_inicio=Calendar.getInstance();
	fecha_inicio.set(Calendar.YEAR, 2022);
	fecha_inicio.set(Calendar.MONTH, Calendar.MARCH);
	fecha_inicio.set(Calendar.DAY_OF_MONTH,1);
	
	for(TCarpeta carpeta: listaSIGJ) {
		System.out.println("Check: "+carpeta.getCarpetaJudicial());
		if(!(carpeta.getCarpetaJudicial().contains("IP")))
			continue;
		
		if(!(Integer.parseInt(carpeta.getIdUnidadGestion())==302)) // Ingresar solo carpetas de IP
			continue;
		
		/*if(!carpeta.getIdCarpeta().equals(1662069316923l))
			continue;*/
		if(!checkCarpetaSIEMP(carpeta.getNumCarpeta(), carpeta.getAnioCarpeta(),
				Integer.parseInt(carpeta.getIdUnidadGestion()), idTipoAsunto)){
			
			System.out.println("Iniciando proceso de insert de datos.....");
			System.out.println("ID: "+carpeta.getIdCarpeta()+", CARPETA="+carpeta.getCarpetaJudicial());
			List<TPresuntoResponsable> imputados=consultaImputados(carpeta.getIdCarpeta());
			List<TTVictima> victimas=consultaVictimas(carpeta.getIdCarpeta());
			if(victimas.isEmpty())
				victimas=victimaNoEspecificad();
			
			if(carpeta.getProcedencia()!=null){
				TCarpeta carpetaProc =aa.consultaCarpetaProcedencia(Long.parseLong(carpeta.getProcedencia()));
				TIngreso ingreso=aa.consultaAntecedenteSIEMP(carpetaProc);
				
				if(ingreso!=null)
					System.out.print("Fecha solicitud ingreso procedencia: "+ingreso.getFechaSolicitudAudiencia());
				
				if (carpetaProc!=null && ingreso!=null)
					/*if(ingreso.getFechaSolicitudAudiencia().before(fecha_inicio.getTime()))
						continue;
					else*/ 
						ingresoAsunto.guardarRadicacionProcedencia(carpeta, ingreso,idTipoAsunto, imputados, victimas);
				
			}
		}
	}
	
	
}


	
	public List<TPresuntoResponsable> consultaImputados(Long idCarpeta)
	{
		return presuntoRepository.findByIdCarpeta(idCarpeta);
	}
	
	public List<TTVictima> consultaVictimas(Long idCarpeta)
	{
		return victimaRepository.findByIdCarpeta(idCarpeta);
	}
	
	public List<TTVictima> victimaNoEspecificad(){
		List<TTVictima> lista=new ArrayList<TTVictima>();
		TTVictima vic= new TTVictima();
		vic.setNombre("No Especificado");
		vic.setAPaterno("No Especificado");
		vic.setAMaterno("No Especifidao");
		vic.setEdad("999");
		vic.setTipoPersona("9");
		vic.setSexo("9");
		vic.setTipoVictima("9");
		lista.add(vic);
		return lista;
		
	}
	
	

}
