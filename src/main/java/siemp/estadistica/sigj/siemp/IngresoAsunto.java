package siemp.estadistica.sigj.siemp;

import java.nio.charset.StandardCharsets;
import static java.nio.charset.StandardCharsets.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import siemp.estadistica.entidades.CEntidad;
import siemp.estadistica.entidades.CSituacion;
import siemp.estadistica.entidades.CTipoAsunto;
import siemp.estadistica.entidades.CTipoAudiencia;
import siemp.estadistica.entidades.TAudienciaImputado;
import siemp.estadistica.entidades.TImputado;
import siemp.estadistica.entidades.TIncompetencia;
import siemp.estadistica.entidades.TIncompetenciaPK;
import siemp.estadistica.entidades.TIngreso;
import siemp.estadistica.entidades.TIngresoPK;
import siemp.estadistica.entidades.TInvestigacionMp;
import siemp.estadistica.entidades.TMedidaProteccion;
import siemp.estadistica.entidades.TMedidaVictima;
import siemp.estadistica.entidades.TPersona;
import siemp.estadistica.entidades.TProcedencia;
import siemp.estadistica.entidades.TSigjCarpeta;
import siemp.estadistica.entidades.TSigjPersona;
import siemp.estadistica.entidades.TSituacionPersona;
import siemp.estadistica.entidades.TSituacionPersonaPK;
import siemp.estadistica.entidades.TTsjdf;
import siemp.estadistica.entidades.TVictima;
import siemp.estadistica.sigj.TransferenciaUtil;
import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;
import siemp.estadistica.sigj.intercambio_jpa.TMedidaLamvlv;
import siemp.estadistica.sigj.intercambio_jpa.TPresuntoResponsable;
import siemp.estadistica.sigj.intercambio_jpa.TTVictima;
import siemp.estadistica.sigj.repository.AudienciaRepository;
import siemp.estadistica.sigj.repository.CarpetaSIGJ;
import siemp.estadistica.sigj.repository.ImputadoRepository;
import siemp.estadistica.sigj.repository.IncompetenciaRepository;
import siemp.estadistica.sigj.repository.IngresoSIEMPRepository;
import siemp.estadistica.sigj.repository.InvestigacionRepository;
import siemp.estadistica.sigj.repository.MedidaProteccionSIGJ;
import siemp.estadistica.sigj.repository.PersonaRepository;
import siemp.estadistica.sigj.repository.PersonaSIGJ;
import siemp.estadistica.sigj.repository.ProcedenciaRepository;
import siemp.estadistica.sigj.repository.SituacionRepository;
import siemp.estadistica.sigj.repository.TMedidaProteccionRepository;
import siemp.estadistica.sigj.repository.TMedidaVictimaRepository;
import siemp.estadistica.sigj.repository.TsjdfRepository;
import siemp.estadistica.sigj.repository.VictimaSIEMPRepository;

public class IngresoAsunto {
	
	
	private IngresoSIEMPRepository siempRepository;
	private TsjdfRepository tsjdfRepository;
	private InvestigacionRepository fgjRepository;
	private PersonaRepository personaRepository;
	private SituacionRepository situacionRepository;
	private ImputadoRepository imputadoRepository;
	private VictimaSIEMPRepository victimaSIEMPRepository;
	private CarpetaSIGJ carpetaSIGJ;
	private PersonaSIGJ personaSIGJ;
	private ProcedenciaRepository procedenciaRepository;
	
	private TMedidaProteccionRepository medidaRepository;
	private MedidaProteccionSIGJ medidasSIGJ;
	private TMedidaVictimaRepository medidaVictimaRepository;
	
	private IncompetenciaRepository incompetenciaRepository;
	
	
	public IngresoAsunto(IngresoSIEMPRepository siempRepository,
			TsjdfRepository tsjdfRepository,
			InvestigacionRepository fgjRepository, PersonaRepository personaRepository,
			SituacionRepository situacionRepository, ImputadoRepository imputadoRepository,
			VictimaSIEMPRepository victimaSIEMPRepository,
			CarpetaSIGJ carpetaSIGJ, PersonaSIGJ personaSIGJ,
			ProcedenciaRepository procedenciaRepository,
			TMedidaProteccionRepository medidaRepository,
			MedidaProteccionSIGJ medidasSIGJ,
			TMedidaVictimaRepository medidaVictimaRepository,
			AudienciaRepository audienciaRepository,
			IncompetenciaRepository incompetenciaRepository) {
		this.siempRepository=siempRepository;
		this.tsjdfRepository=tsjdfRepository;
		this.fgjRepository=fgjRepository;
		this.personaRepository=personaRepository;
		this.situacionRepository=situacionRepository;
		this.imputadoRepository=imputadoRepository;
		this.victimaSIEMPRepository=victimaSIEMPRepository;
		this.carpetaSIGJ=carpetaSIGJ;
		this.personaSIGJ=personaSIGJ;
		this.procedenciaRepository=procedenciaRepository;
		this.medidaRepository=medidaRepository;
		this.medidasSIGJ=medidasSIGJ;
		this.medidaVictimaRepository=medidaVictimaRepository;
		this.incompetenciaRepository=incompetenciaRepository;
	}
	
	
	
	 public TTsjdf guardarRadicacion(TCarpeta carpeta, 
			 List<TPresuntoResponsable> imputados,
			 List<TTVictima> victimas, int idTipoAsunto, String consignacion) {
	       
       System.out.println("se inicio el proceso de radicación de asunto");
       TIngreso ingreso=guardarIngreso(carpeta,idTipoAsunto, consignacion);
       TTsjdf tsjdf=ingreso.getTTsjdf();
       
       for(TPresuntoResponsable presunto: imputados){
    	   guardarImputado(presunto, tsjdf.getIdTsjdf(), consignacion);
       }
       
       if(!(victimas.isEmpty()))
       for(TTVictima vic: victimas) {
    	   
    	   guardarVictima(vic, tsjdf.getIdTsjdf());
       }
      
       System.out.println("ID de asunto: "+tsjdf.getIdTsjdf());
       return tsjdf;

    }
	 
	 private TIngreso guardarIngreso(TCarpeta ingreso, int idTipoAsunto, String consignacion) 
	    {
	       TTsjdf tsjdf=null;
	       TIngreso tingreso=null;
        	
	       System.out.println( "anio: "+ingreso.getAnioCarpeta()+", carpeta: "+ingreso.getAnioCarpeta());
           TIngreso ingreso_aux=null;
        
           
           int idEntidad=Integer.parseInt(ingreso.getIdUnidadGestion());
           idEntidad=TransferenciaUtil.getIDEntidad(idEntidad);
           tsjdf=generaIdentificadorTSJDF();
           TIngresoPK pk=new TIngresoPK(tsjdf.getIdTsjdf(), idEntidad);
           CEntidad entidad= new CEntidad();
           entidad.setIdEntidad(idEntidad);
           CTipoAsunto asunto=new CTipoAsunto();
           asunto.setIdTipoAsunto(idTipoAsunto);
           
           
           tingreso=new TIngreso();
           tingreso.setAnioCarpeta(ingreso.getAnioCarpeta());
           tingreso.setNumCarpetaAdmin(ingreso.getNumCarpeta());
           tingreso.setFechaRegistro(new Date());
           tingreso.setFechaSolicitudAudiencia(ingreso.getFechaSolicitud());
           tingreso.setNumPartida(ingreso.getCarpetaJudicial());
           tingreso.setCTipoAsunto(asunto);
           tingreso.setId(pk);
           tingreso.setCEntidad(entidad);
           tingreso.setEstatus('1');
           tingreso.setSituacionExpediente(5);
           tingreso.setFechaRegistro(new Date());
           tingreso.setTTsjdf(tsjdf);
           siempRepository.save(tingreso);
           
           TSigjCarpeta carpeta=new TSigjCarpeta();
           carpeta.setIdCarpeta(ingreso.getIdCarpeta());
           carpeta.setIdTsjdf(tsjdf.getIdTsjdf());
           
           carpetaSIGJ.save(carpeta);
           
           guardarInvestigacionPrevia(ingreso.getNumInvestigacion(), tsjdf, consignacion);
	           
	       
	       return tingreso;
	    } 
	 
	 
	private TTsjdf generaIdentificadorTSJDF()
    {
		Date now=new Date();
        Long idTsjdf=generaIDTSJDF(11, '7');
        TTsjdf tsjdf=new TTsjdf(idTsjdf,now, 11, '1');
        tsjdfRepository.save(tsjdf);
        System.out.println("--- "+now+" ----------------");
        return tsjdf;
    }
	
	private Long generaIDTSJDF(int idMateria, char tipoIngreso) {
        Calendar cal=Calendar.getInstance();
        int anio=cal.get(Calendar.YEAR);
        
        List<Object[]> lista=siempRepository.consultaFolio();
       
        System.out.println("numero encontrado: "+lista.get(0)[0]);
        int folio=Integer.parseInt(String.valueOf(lista.get(0)[0]))+1;
        String folio_aux=getFormatoNumeros(folio, 6);
        String idTSJDF=String.valueOf(anio)+String.valueOf(idMateria)+String.valueOf(tipoIngreso)+folio_aux;
        return Long.parseLong(idTSJDF);
    }


	
	private String getFormatoNumeros(Integer numero, int numDigitos)
    {
        String formato_aux="";
        for(int i=0;i<numDigitos;i++)
        {
            formato_aux=formato_aux.concat("0");
        }
        NumberFormat formatter = new DecimalFormat(formato_aux);
        String aux = formatter.format(numero);
        return aux;
    }
	
	private void guardarInvestigacionPrevia(String numInvestigacion, TTsjdf tsjdf, String consignacion)
    {
		System.out.println("TSJDF: "+tsjdf.getIdTsjdf());
        TInvestigacionMp mp=new TInvestigacionMp();
        mp.setNumInvestigacion(numInvestigacion);
        mp.setIdTsjdf(tsjdf.getIdTsjdf());
        mp.setTipoInvestigacion("1");
        mp.setConsignacion(consignacion);
        mp.setIdMateria(11);
        fgjRepository.save(mp);
        
    }
	
	
	
	private void guardarVictima(TTVictima victima, Long idTsjdf)
    {
        TPersona persona=new TPersona();
        
        byte[] aux1 = victima.getNombre().getBytes(ISO_8859_1); 
        String nombre_aux = new String(aux1, UTF_8); 
        byte[] aux2 = victima.getAPaterno().getBytes(ISO_8859_1); 
        String paterno_aux = new String(aux2, UTF_8); 
        byte[] aux3 = victima.getAMaterno().getBytes(ISO_8859_1); 
        String materno_aux = new String(aux3, UTF_8); 
      
        persona.setNombre(nombre_aux);
        persona.setAPaterno(paterno_aux);
        persona.setAMaterno(materno_aux);
        
        System.out.println("ID: "+victima.getIdPersona()+", nombre: "+persona.getNombre()+
        		", Paterno: "+persona.getAPaterno()+", materno: "+persona.getAMaterno());
        
        if(!victima.getEdad().trim().isEmpty())
        	persona.setEdad(Integer.parseInt(victima.getEdad()));
        persona.setEstatus("1");
        String aux_tipo="1";
        if(victima.getTipoPersona()==null || victima.getTipoPersona().equals("")) 
        	aux_tipo="9";
        else if(victima.getTipoPersona().equals("moral"))
        	aux_tipo="2";
        persona.setTipoPersona(aux_tipo);
        persona.setSexo(victima.getSexo());
       
        char sexo_aux='9';
        
        if(victima.getSexo()==null || victima.getSexo().trim().equals(""))
        	sexo_aux=verificarSexoNombre(victima.getNombre());
        else if(victima.getSexo().equals("masculino") || victima.getSexo().equals("M"))
        	sexo_aux='M';
        else if(victima.getSexo().equals("femenino") || victima.getSexo().equals("F"))
        	sexo_aux='F';
        else 
        	sexo_aux=verificarSexoNombre(victima.getNombre());
        	
        persona.setSexo(String.valueOf(sexo_aux));
        
        
        personaRepository.save(persona);
        
        TSigjPersona persona_sigj= new TSigjPersona();
        persona_sigj.setIdCarpetaSigj(victima.getIdCarpeta());
        persona_sigj.setIdPersona(persona.getIdPersona());
        persona_sigj.setIdPersonaSigj(victima.getIdPersona());
        persona_sigj.setIdTsjdf(idTsjdf);
        
        personaSIGJ.save(persona_sigj);
        
        
        TSituacionPersonaPK pk=new TSituacionPersonaPK();
        pk.setIdPersona(persona.getIdPersona());
        pk.setIdTsjdf(idTsjdf);
        CSituacion situacion=new CSituacion();
        situacion.setIdSituacion(2);
        
        TSituacionPersona sp=new TSituacionPersona();
        sp.setCSituacion(situacion);
        sp.setId(pk);
        sp.setEstatus("1");
        
        situacionRepository.save(sp);
        
        String aux_tipo_victima="9";
        if(victima.getTipoVictima().equals("AGRAVIADO(s)"))
        	aux_tipo_victima="3";
        else if(victima.getTipoVictima().equals("VÍCTIMA"))
        	aux_tipo_victima="1";
        else if(victima.getTipoVictima().equals("QUERELLANTE"))
        	aux_tipo_victima="4";
        else if(victima.getTipoVictima().equals("DENUNCIANTE(s)"))
        	aux_tipo_victima="5";
        
        TVictima vic= new TVictima();
        vic.setIdPersona(persona.getIdPersona());
        vic.setIdRelacionVictima(99);
        vic.setTipoVictima(aux_tipo_victima);
        vic.setComplemento("0");
        
        victimaSIEMPRepository.save(vic);
        
        
    }

	
	
	
	public void guardarImputado(TPresuntoResponsable responsable, Long idTsjdf, String consignacion)
    {
        TPersona persona=new TPersona();
        if(responsable==null)
        	return;
        byte[] aux1 = responsable.getNombre().getBytes(UTF_8); 
        String nombre_aux = new String(aux1, UTF_8); 
        byte[] aux2 = responsable.getAPaterno().getBytes(UTF_8); 
        String paterno_aux = new String(aux2, UTF_8); 
        byte[] aux3 = responsable.getAMaterno().getBytes(UTF_8); 
        String materno_aux = new String(aux3, UTF_8); 
        
        persona.setNombre(nombre_aux);
        persona.setAPaterno(paterno_aux);
        persona.setAMaterno(materno_aux);
        
        System.out.println("ID: "+responsable.getIdPersona()+", nombre: "+persona.getNombre()+
        		", Paterno: "+persona.getAPaterno()+", materno: "+persona.getAMaterno());
        
        
        if(!responsable.getEdad().trim().isEmpty())
        	persona.setEdad(Integer.parseInt(responsable.getEdad()));
        persona.setEstatus("1");
        
        String aux_tipo="1";
        if(responsable.getTipoPersona()==null || responsable.getTipoPersona().equals("")) 
        	aux_tipo="9";
        else if(responsable.getTipoPersona().equals("moral"))
        	aux_tipo="2";
        persona.setTipoPersona(aux_tipo);
       
        char sexo_aux='9';
        
        if(responsable.getGenero()==null || responsable.getGenero().trim().equals(""))
        	sexo_aux=verificarSexoNombre(responsable.getNombre());
        else if(responsable.getGenero().equals("masculino") || responsable.getGenero().equals("M"))
        	sexo_aux='M';
        else if(responsable.getGenero().equals("femenino") || responsable.getGenero().equals("F"))
        	sexo_aux='F';
        else 
        	sexo_aux=verificarSexoNombre(responsable.getNombre());
        	
        persona.setSexo(String.valueOf(sexo_aux));
        
        personaRepository.save(persona);
        
        TSigjPersona persona_sigj= new TSigjPersona();
        persona_sigj.setIdCarpetaSigj(responsable.getIdCarpeta());
        persona_sigj.setIdPersona(persona.getIdPersona());
        persona_sigj.setIdPersonaSigj(responsable.getIdPersona());
        persona_sigj.setIdTsjdf(idTsjdf);
        
        personaSIGJ.save(persona_sigj);
        
        
        TSituacionPersonaPK pk=new TSituacionPersonaPK();
        pk.setIdPersona(persona.getIdPersona());
        pk.setIdTsjdf(idTsjdf);
        CSituacion situacion=new CSituacion();
        situacion.setIdSituacion(1);
        
        TSituacionPersona sp=new TSituacionPersona();
        sp.setCSituacion(situacion);
        sp.setId(pk);
        sp.setEstatus("1");
        
        situacionRepository.save(sp);
        
        TImputado imputado=new TImputado();
        imputado.setIdPersona(persona.getIdPersona());
        imputado.setConsignacion(consignacion);
        imputado.setSituacionLibertad("0");
        imputado.setComplemento("0");
        imputadoRepository.save(imputado);
        
        
    }

	
	private Character verificarSexoNombre(String nombre)
    {
		System.out.println("Iniciando verificacion de sexo");
        int porcentaje_confianza=60;
        int minimo_registros=25;
        int longitud_minima=3;
        if(nombre==null || nombre.length()<longitud_minima)
            return '9';
        List<Object[]> lista=personaRepository.consultaSexoNombre(nombre);
        if(lista==null || lista.isEmpty())
            return '9';
        System.out.println("lista:  "+lista.size());
        int mujeres=0;
        int hombres=0;
        for(Object[] obj:lista){
        	System.out.println("obj[0]="+obj[0]+" ,obj[1]="+obj[1]);
            if(obj[0].toString().equals(String.valueOf('M')))
                hombres=Integer.parseInt(obj[1].toString());
            else if(obj[0].toString().equals(String.valueOf('F')))
                mujeres=Integer.parseInt(obj[1].toString());
        }
        
        int total=hombres+mujeres;
        System.out.println( "total: "+total);
        if(total<minimo_registros)
            return '9';
        int porcentaje_hombres=(int)(((float)hombres/total)*100);
        int porcentaje_mujeres=(int)(((float)mujeres/total)*100);
        System.out.println("total: "+total+",%M="+porcentaje_hombres+", %F="+porcentaje_mujeres);
        if(porcentaje_hombres>porcentaje_confianza)
            return 'M';
        else if(porcentaje_mujeres>porcentaje_confianza)
            return 'F';
        else
            return '9';
            
    }
	
	public TTsjdf guardarRadicacionProcedencia(TCarpeta carpeta, 
			 TIngreso antecedente, int idTipoAsunto, List<TPresuntoResponsable> imputados, 
			 List<TTVictima> victimas)
	{
		   System.out.println("se inicio el proceso de radicación con Procedencia: ");
		   System.out.println("ID antecedente: "+antecedente.getTTsjdf().getIdTsjdf());
		   /*List<TSigjPersona> sigj=personaSIGJ.findByIdTsjdfSigj(antecedente.getId().getIdTsjdf());
		   if(sigj==null || sigj.isEmpty())
			   return null;*/
	       TIngreso ingreso=guardarIngreso(carpeta,idTipoAsunto, "0");
	       TTsjdf tsjdf=ingreso.getTTsjdf();
	       
	       boolean resultado=guardarImputadoProcedencia(tsjdf, imputados, antecedente);
	       guardarVictimaProcedencia(tsjdf, victimas, antecedente);
	       
	       if(!resultado) /* Si no se guardo ningun dato de imputado cancelar el registro */
	       {
	    	   cancelarRegistroIngreso(ingreso);
	    	   return null;
	       }
	    	   
	    	   
	       if(idTipoAsunto==7 || idTipoAsunto==10)
	    	   guardarProcedencia(tsjdf, antecedente, carpeta);
	       else if(idTipoAsunto==2)
	    	   guardarIncompetencia(tsjdf, antecedente, ingreso);
	       
	       return tsjdf;
	       
	}
	
	
	public void cancelarRegistroIngreso(TIngreso ingreso) {
		ingreso.setEstatus('0');
		siempRepository.save(ingreso);
	}
	
	public TPersona obtenerPersonaSIEMP(TPresuntoResponsable pp, Long idTsjdf) {
		List<TSituacionPersona> listaImputadosSIEMP=situacionRepository.findImputados(idTsjdf);
		TPersona psiemp=null;
		if(listaImputadosSIEMP==null || listaImputadosSIEMP.isEmpty())
			return null;
		for(TSituacionPersona sp: listaImputadosSIEMP) {
			TPersona p=personaRepository.findByIdPersona(sp.getId().getIdPersona());
			if(p.getNombre().trim().equals(pp.getNombre().trim())) {
				psiemp=p;
				break;
			}
		}
		return psiemp;
	}
	
	
	public boolean guardarImputadoProcedencia(TTsjdf tsjdf, List<TPresuntoResponsable> imputados, 
			TIngreso antecedente ) {
		TSigjPersona sigj;
		TSituacionPersonaPK pk;
		TSituacionPersona sp_nueva;
		boolean flag=false;
		for(TPresuntoResponsable pp: imputados) {
			/*sigj=personaSIGJ.findByIdPersonaSigj(pp.getIdPersona(), antecedente.getTTsjdf().getIdTsjdf());*/
			TPersona p=obtenerPersonaSIEMP(pp, antecedente.getId().getIdTsjdf());
			if(p==null) continue;
			
			flag=true;
			System.out.println("id_tsjdf: "+antecedente.getTTsjdf().getIdTsjdf()+", id_persona: "+p.getIdPersona());
			
			pk=new TSituacionPersonaPK();
			pk.setIdPersona(p.getIdPersona());
			pk.setIdTsjdf(tsjdf.getIdTsjdf());
			
			CSituacion csituacion=new CSituacion();
			csituacion.setIdSituacion(1);
			
			sp_nueva=new TSituacionPersona();
    	    sp_nueva.setId(pk);
    	    sp_nueva.setCSituacion(csituacion);
    	    sp_nueva.setEstatus("1");
    	   
    	   situacionRepository.save(sp_nueva);
		}
		return flag;
	}
	
	public void guardarVictimaProcedencia(TTsjdf tsjdf, List<TTVictima> victimas, 
			TIngreso antecedente ) {
		TSigjPersona sigj;
		TSituacionPersonaPK pk;
		TSituacionPersona sp_nueva;
		List<TSituacionPersona> spList=situacionRepository.findVictimas(antecedente.getId().getIdTsjdf());
		if(spList==null || spList.isEmpty())
			return;
		
		for(TSituacionPersona pp: spList) {
			
			System.out.println("id_tsjdf: "+antecedente.getTTsjdf().getIdTsjdf()+", id_persona: "+pp.getId().getIdPersona());
			
			pk=new TSituacionPersonaPK();
			pk.setIdPersona(pp.getId().getIdPersona());
			pk.setIdTsjdf(tsjdf.getIdTsjdf());
			
			CSituacion csituacion=new CSituacion();
			csituacion.setIdSituacion(2);
			
			sp_nueva=new TSituacionPersona();
    	    sp_nueva.setId(pk);
    	    sp_nueva.setCSituacion(csituacion);
    	    sp_nueva.setEstatus("1");
    	   
    	   situacionRepository.save(sp_nueva);
		}
	}
	
	public void guardarProcedencia(TTsjdf tsjdf, TIngreso antecedente, TCarpeta carpeta)
	{
		  TProcedencia procedencia=new TProcedencia();
	       procedencia.setIdTsjdf(tsjdf.getIdTsjdf());
	       procedencia.setAnioExpediente(antecedente.getAnioCarpeta());
	       procedencia.setEstatus("1");
	       procedencia.setNumInvestigacion(carpeta.getNumInvestigacion());
	       procedencia.setNumExpediente(antecedente.getNumCarpetaAdmin());
	       procedencia.setIdTsjdfOrigen(antecedente.getTTsjdf().getIdTsjdf());
	       if(antecedente.getCTipoAsunto().getIdTipoAsunto()==10) /* Juicio Oral */
	    	   procedencia.setFormaProceso("2");
	       else
	    	   procedencia.setFormaProceso("1");
	       
	       procedencia.setIdEntidad(antecedente.getCEntidad().getIdEntidad());
	       procedenciaRepository.save(procedencia);

	}
	
	public void guardarIncompetencia(TTsjdf tsjdf, TIngreso antecedente, TIngreso ingreso) {
		  TIncompetenciaPK pk_inc= new TIncompetenciaPK();
	   	  pk_inc.setIdTsjdf(tsjdf.getIdTsjdf());
	   	  pk_inc.setIdEntidad(ingreso.getId().getIdEntidad());
	   	  
	   	  TIncompetencia inco=new TIncompetencia();
	   	  inco.setId(pk_inc);
	   	  inco.setAceptaIncompetencia("9");
	   	  inco.setIdMateria(11);
	   	  inco.setIdTsjdfAnterior(antecedente.getTTsjdf().getIdTsjdf());
	   	  inco.setNumJuzgado(antecedente.getId().getIdEntidad());
	   	  inco.setNumPartida(antecedente.getNumPartida());
	   	   
	   	  incompetenciaRepository.save(inco);
	}
	
	 public TTsjdf guardarRadicacionAntecedenteSIEMP(TCarpeta carpeta, 
			 TIngreso antecedente, int idTipoAsunto) {
		   System.out.println("se inicio el proceso de radicación de asunto con antecedente: ");
		   System.out.println("ID antecedente: "+antecedente.getTTsjdf().getIdTsjdf());
	       TIngreso ingreso=guardarIngreso(carpeta,idTipoAsunto, "0");
	       TTsjdf tsjdf=ingreso.getTTsjdf();
	       
	       List<TSituacionPersona> sp=situacionRepository.findByIDTsjdf(antecedente.getTTsjdf().getIdTsjdf());
	       TSituacionPersonaPK pk;
	       TSituacionPersona sp_nueva;
	       for(TSituacionPersona aux: sp) {
	    	   System.out.println("idTsjdf: "+aux.getId().getIdTsjdf()+
	    			   ", idPersona="+aux.getId().getIdPersona());
	    	   pk=new TSituacionPersonaPK();
	    	   pk.setIdPersona(aux.getId().getIdPersona());
	    	   pk.setIdTsjdf(tsjdf.getIdTsjdf());
	    	   
	    	   sp_nueva=new TSituacionPersona();
	    	   sp_nueva.setId(pk);
	    	   sp_nueva.setCSituacion(aux.getCSituacion());
	    	   sp_nueva.setEstatus("1");
	    	   
	    	   situacionRepository.save(sp_nueva);
	       }
	       if(idTipoAsunto==7 || idTipoAsunto==10)
	       {
		       TProcedencia procedencia=new TProcedencia();
		       procedencia.setIdTsjdf(tsjdf.getIdTsjdf());
		       procedencia.setAnioExpediente(antecedente.getAnioCarpeta());
		       procedencia.setEstatus("1");
		       procedencia.setNumInvestigacion(carpeta.getNumInvestigacion());
		       procedencia.setNumExpediente(antecedente.getNumCarpetaAdmin());
		       procedencia.setIdTsjdfOrigen(antecedente.getTTsjdf().getIdTsjdf());
		       if(antecedente.getCTipoAsunto().getIdTipoAsunto()==10) /* Juicio Oral */
		    	   procedencia.setFormaProceso("2");
		       else
		    	   procedencia.setFormaProceso("1");
		       
		       procedencia.setIdEntidad(antecedente.getCEntidad().getIdEntidad());
		       procedenciaRepository.save(procedencia);
		       
	       }else if(idTipoAsunto==2) {
	    	   TIncompetenciaPK pk_inc= new TIncompetenciaPK();
	    	   pk_inc.setIdTsjdf(tsjdf.getIdTsjdf());
	    	   pk_inc.setIdEntidad(ingreso.getId().getIdEntidad());
	    	   
	    	   
	    	   TIncompetencia inco=new TIncompetencia();
	    	   inco.setId(pk_inc);
	    	   inco.setAceptaIncompetencia("9");
	    	   inco.setIdMateria(11);
	    	   inco.setIdTsjdfAnterior(antecedente.getTTsjdf().getIdTsjdf());
	    	   inco.setNumJuzgado(antecedente.getId().getIdEntidad());
	    	   inco.setNumPartida(antecedente.getNumPartida());
	    	   
	    	   incompetenciaRepository.save(inco);
	    	   
	       }
	       
	       
	       return tsjdf;
	       
	       
	       
		 
	 }
	
	 
	 public TTsjdf guardarRadicacionAntecedenteSIGJ(TCarpeta carpeta, 
			 List<TPresuntoResponsable> imputados,
			 List<TTVictima> victimas, int idTipoAsunto, TCarpeta antecedente) {
		   System.out.println("se inicio el proceso de radicación de asunto con antecedente SIGJ: ");
		   System.out.println("ID antecedente SIGJ: "+antecedente.getCarpetaJudicial());
	       
		   TTsjdf tsjdf=guardarRadicacion(carpeta, imputados, victimas, idTipoAsunto, "0");
		   
		   if(idTipoAsunto==7 || idTipoAsunto==10)
		   {
		       TProcedencia procedencia=new TProcedencia();
		       procedencia.setIdTsjdf(tsjdf.getIdTsjdf());
		       procedencia.setAnioExpediente(antecedente.getAnioCarpeta());
		       procedencia.setEstatus("1");
		       procedencia.setNumInvestigacion(carpeta.getNumInvestigacion());
		       procedencia.setNumExpediente(antecedente.getNumCarpeta());
		       procedencia.setIdTsjdfOrigen(null);
		       procedencia.setFormaProceso(null);
		       
		       procedencia.setIdEntidad(TransferenciaUtil.
		    		   getIDEntidad(Integer.parseInt(antecedente.getIdUnidadGestion())));
		       
		       
		       procedenciaRepository.save(procedencia);
		   }else if(idTipoAsunto==2) {
	    	   TIncompetenciaPK pk_inc= new TIncompetenciaPK();
	    	   pk_inc.setIdTsjdf(tsjdf.getIdTsjdf());
	    	   pk_inc.setIdEntidad(TransferenciaUtil.getIDEntidad(
	    			   Integer.parseInt(carpeta.getIdUnidadGestion())));
	    	   
	    	   TIncompetencia inco=new TIncompetencia();
	    	   inco.setAceptaIncompetencia("9");
	    	   inco.setIdMateria(11);
	    	   inco.setNumJuzgado(TransferenciaUtil.getIDEntidad(
	    			   Integer.parseInt(antecedente.getIdUnidadGestion())));
	    	   inco.setNumPartida(antecedente.getCarpetaJudicial());
	    	   inco.setId(pk_inc);
	    	   incompetenciaRepository.save(inco);
	    	   
	       }
	       return tsjdf;
		 
	 }
	 
	 
	 public TTsjdf guardarMedidaProteccionCNPP(TCarpeta carpeta, 
			 List<TPresuntoResponsable> imputados,
			 List<TTVictima> victimas, int idTipoAsunto) {
		 
		 System.out.println("se inicio el proceso de radicación MEDIDA DE PROTECCION CNPP: ");
		 TTsjdf tsjdf=guardarRadicacion(carpeta, imputados, victimas, idTipoAsunto,"0");
		 
		 TMedidaProteccion medida=new TMedidaProteccion();
		 medida.setIdTsjdf(tsjdf.getIdTsjdf());
		 medida.setIdMedidaProteccion("1");
		 
		 medidaRepository.save(medida);
		 Date fechaResolucion=null;
		/* for(TTVictima vic: victimas) {
			 
			 fechaResolucion=registrar_medidas_proteccion(vic,tsjdf,carpeta.getFechaSolicitud());
		 }
		 */
	/*	 if(fechaResolucion!=null)
		 {
			 Calendar fecha_aux=Calendar.getInstance();
			 fecha_aux.setTime(fechaResolucion);
			
			 registrarAudiencia(tsjdf.getIdTsjdf(), fecha_aux.getTime(),fecha_aux.getTime(),
					 fecha_aux.getTime(), 9, -1, "2" );
		 }
		 */

		 return tsjdf;
	 }

	 
	 public TTsjdf guardarMedidaProteccionLAMVLV(TCarpeta carpeta, 
			 List<TPresuntoResponsable> imputados,
			 List<TTVictima> victimas, int idTipoAsunto) {
		 
		 System.out.println("se inicio el proceso de radicación MEDIDA DE PROTECCION LAMVLV: ");
		 TTsjdf tsjdf=guardarRadicacion(carpeta, imputados, victimas, idTipoAsunto,"0");
		 
		 TMedidaProteccion medida=new TMedidaProteccion();
		 medida.setIdTsjdf(tsjdf.getIdTsjdf());
		 medida.setIdMedidaProteccion("2");
		 
		 medidaRepository.save(medida);
		 Date fechaResolucion=null;
		 for(TTVictima vic: victimas) {
			 
			 fechaResolucion=registrar_medidas_proteccion(vic,tsjdf,carpeta.getFechaSolicitud());
		 }
		 
	/*	 if(fechaResolucion!=null)
		 {
			 Calendar fecha_aux=Calendar.getInstance();
			 fecha_aux.setTime(fechaResolucion);
			 
			 registrarAudiencia(tsjdf.getIdTsjdf(), fecha_aux.getTime(),fecha_aux.getTime(),
					 fecha_aux.getTime(), 9, -1, "2" );
		 }*/
		 

		 return tsjdf;
	 }
	 
	
	 
	 
	
	 
	 public Date registrar_medidas_proteccion(TTVictima vic, TTsjdf tsjdf, 
			 Date fechaSolicitud) {
		 
		 List<TMedidaLamvlv> medidas=medidasSIGJ.findByIdPersona(vic.getIdPersona());
		 if(medidas.isEmpty())
			 return null;
		 TSigjPersona persona_tmp=personaSIGJ.findByIdPersonaSigj(vic.getIdPersona(), tsjdf.getIdTsjdf());
		 if(persona_tmp==null)
			 return null;
		 
		 System.out.println("ID TSJDF: "+tsjdf.getIdTsjdf()+ ", "+
				 "ID_VICTIMA_SIEMP: "+persona_tmp.getIdPersona());
		 int tipo_medida=0;
		 TMedidaVictima mv=null;
		 Date fechaResolucion=null;
		 for(TMedidaLamvlv aux: medidas) {
			 tipo_medida=TransferenciaUtil.getNumFromRoma(aux.getTipoMedida());
			 tipo_medida=tipo_medida+24;
			 mv=new TMedidaVictima();
			 mv.setIdPersona(persona_tmp.getIdPersona());
			 mv.setIdTsjdf(tsjdf.getIdTsjdf());
			 mv.setIdTipoMedida(tipo_medida);
			 mv.setFechaSolicitudMedida(fechaSolicitud);
			 mv.setOtorgamiento(aux.getResolucionMedida());
			 mv.setEstatus("1");
			 medidaVictimaRepository.save(mv);
			 
			 fechaResolucion=mv.getFechaSolicitudMedida();
		 }
		 
		
		 
		 return fechaSolicitud;
	 }
	 
	
	
	
	
}
