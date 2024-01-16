package siemp.estadistica.sigj;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import siemp.estadistica.sigj.intercambio_jpa.TCarpeta;
import siemp.estadistica.sigj.repository.ActoProcesalRepository;
import siemp.estadistica.sigj.repository.AudienciaRepository;
import siemp.estadistica.sigj.repository.CarpetaSIGJ;
import siemp.estadistica.sigj.repository.CarpetasRepository;
import siemp.estadistica.sigj.repository.ImpugnacionRepository;
import siemp.estadistica.sigj.repository.ImputadoRepository;
import siemp.estadistica.sigj.repository.IncompetenciaRepository;
import siemp.estadistica.sigj.repository.IngresoSIEMPRepository;
import siemp.estadistica.sigj.repository.InvestigacionRepository;
import siemp.estadistica.sigj.repository.MedidaProteccionCNPP;
import siemp.estadistica.sigj.repository.MedidaProteccionSIGJ;
import siemp.estadistica.sigj.repository.PersonaRepository;
import siemp.estadistica.sigj.repository.PersonaSIGJ;
import siemp.estadistica.sigj.repository.PresuntoRepository;
import siemp.estadistica.sigj.repository.ProcedenciaRepository;
import siemp.estadistica.sigj.repository.SituacionRepository;
import siemp.estadistica.sigj.repository.TMedidaProteccionRepository;
import siemp.estadistica.sigj.repository.TMedidaVictimaRepository;
import siemp.estadistica.sigj.repository.TecnicaRepository;
import siemp.estadistica.sigj.repository.TsjdfRepository;
import siemp.estadistica.sigj.repository.VictimaRepository;
import siemp.estadistica.sigj.repository.VictimaSIEMPRepository;
import siemp.estadistica.sigj.service.ActualizarPresuntos;
import siemp.estadistica.sigj.service.ConsultaDatosService;
import siemp.estadistica.sigj.siemp.Audiencia;
import siemp.estadistica.sigj.siemp.IngresoAsunto;
import siemp.estadistica.sigj.siemp.MedidasProteccion;
import siemp.estadistica.sigj.siemp.RegistroCateos;
import siemp.estadistica.sigj.siemp.RegistroImpugnacion;

@SpringBootApplication
@EntityScan("siemp.estadistica.*")
public class TransferenciaInformacionApplication implements CommandLineRunner{

	@Autowired
	private CarpetasRepository carpetaRepository;
	
	@Autowired
	private IngresoSIEMPRepository siempRepository;
	
	@Autowired
	private TsjdfRepository tsjdfRepository; 
	
	@Autowired
	private InvestigacionRepository fgjRepository;
	
	@Autowired
	private PresuntoRepository presuntoRepository;
	
	@Autowired
	private SituacionRepository situacionRepository;
	
	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private ImputadoRepository imputadoRepository;
	@Autowired
	private VictimaRepository victimaRepository;
	@Autowired
	private VictimaSIEMPRepository victimaSIEMPRepository;
	@Autowired
	private CarpetaSIGJ carpetaSIGJ;
	@Autowired
	private PersonaSIGJ personaSIGJ;
	
	@Autowired
	private ProcedenciaRepository procedenciaRepository;
	
	@Autowired
	private TMedidaProteccionRepository medidaRepository;
	@Autowired
	private MedidaProteccionSIGJ medidasSIGJ;
	@Autowired
	private TMedidaVictimaRepository medidaVictimaRepository;
	@Autowired
	private AudienciaRepository audienciaRepository;
	  
	@Autowired
	private TecnicaRepository tecnicaRepository ;
	@Autowired
	private IncompetenciaRepository incompetenciaRepository;
	@Autowired
	private ImpugnacionRepository impugnaRepository;
	@Autowired
	private ActoProcesalRepository actoProcesalRepository;
	
	@Autowired
	private MedidaProteccionCNPP medidasCnpp;
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(TransferenciaInformacionApplication.class, args);
	}
	
	@Override
    public void run(String... args) {
       System.out.println("Ok");
       ConsultaDatosService service=new ConsultaDatosService(carpetaRepository, 
    		   siempRepository, presuntoRepository, victimaRepository);
       IngresoAsunto ingreso=new IngresoAsunto(siempRepository, tsjdfRepository, fgjRepository,
    		   personaRepository,situacionRepository, imputadoRepository,
    		   victimaSIEMPRepository, carpetaSIGJ, personaSIGJ, 
    		   procedenciaRepository, medidaRepository, medidasSIGJ,
    		   medidaVictimaRepository, audienciaRepository, incompetenciaRepository);
       
       RegistroCateos cateo= new RegistroCateos(ingreso, tecnicaRepository);
       RegistroImpugnacion impugnacion= new RegistroImpugnacion(ingreso, impugnaRepository);
       Audiencia audiencia=new Audiencia(audienciaRepository);
       MedidasProteccion medidas=new MedidasProteccion(audiencia, 
    		   medidasSIGJ, carpetaSIGJ, personaSIGJ, medidaVictimaRepository, 
    		   victimaRepository, actoProcesalRepository, medidasCnpp);
       
     /*  ActualizarPresuntos update=new ActualizarPresuntos(carpetaSIGJ, personaSIGJ, situacionRepository, ingreso);*/
       
       
      int mes=11;
      int anio=2023;
       
     /*
      List<TCarpeta> lista=service.consultaCarpetasByTipo(26, anio,mes);
       service.iniciarIngresoCarpeta(lista, ingreso, "1");
      
     List<TCarpeta> lista_ejecucion=service.consultaCarpetasByTipo(67, anio,mes);
     service.ingresoEjecucionFixed(lista_ejecucion, ingreso);
     
     lista_ejecucion=service.consultaCarpetasByTipo(67, anio,mes);
     service.iniciarIngresoEjecucion(lista_ejecucion, ingreso);
     
     
       
       List<TCarpeta> lista_medidas=service.consultaCarpetasByTipo(252, anio, mes);
       service.ingresoMedidaProteccion(lista_medidas, ingreso);
      
       List<TCarpeta> cateos=service.consultaCarpetasByTipo(9, anio, mes);
       service.ingresoCateo(cateos, ingreso, cateo,1);
   
      
     List<TCarpeta> ordenAprension=service.consultaCarpetasByTipo(97, anio, mes);
      service.iniciarIngresoCarpeta(ordenAprension, ingreso, "0");
       
      List<TCarpeta> actos=service.consultaCarpetasByTipo(69, anio, mes);
      service.ingresoCateo(actos, ingreso, cateo, 99); 
      
      actos=service.consultaCarpetasByTipo(56, anio, mes);
      service.ingresoCateo(actos, ingreso, cateo, 5);// Toma de muestra 
      
    
     List<TCarpeta> ingresoSinDetenido=service.consultaCarpetasByTipo(1, anio, mes);
      service.iniciarIngresoCarpeta(ingresoSinDetenido, ingreso, "0");
       
     List<TCarpeta> incompetencia=service.consultaCarpetasByTipo(67, anio, mes); 
       service.iniciarIngresoIncompetencia(incompetencia, ingreso);
      
      
       List<TCarpeta> te=service.consultaCarpetasByTipo(67, anio, mes); 
       service.iniciarIngresoTE(te, ingreso);
       
     
     List<TCarpeta> impugna=service.consultaCarpetasByTipo(8, anio, mes); 
       service.ingresoImpugnacion(impugna, ingreso, impugnacion);
     
       
       List<TCarpeta> listaCNPP=service.consultaCarpetasByTipo(52, anio, mes);
       service.ingresoMedidaProteccionCNPP(listaCNPP, ingreso);
      
       
      List<TCarpeta> otros_tipos=service.consultaCarpetasByTipo(23, anio, mes); 
       service.iniciarIngresoOtros(otros_tipos, ingreso,"0");
       
       List<TCarpeta> providencias_precautorias=service.consultaCarpetasByTipo(44, anio, mes); 
       service.iniciarIngresoOtros(providencias_precautorias, ingreso,"0");
      
       otros_tipos=service.consultaCarpetasByTipo(248, anio, mes); 
       service.iniciarIngresoOtros(otros_tipos, ingreso,"0");
       
       otros_tipos=service.consultaCarpetasByTipo(10, anio, mes); 
       service.iniciarIngresoOtros(otros_tipos, ingreso,"0");
       
       otros_tipos=service.consultaCarpetasByTipo(103, anio, mes); 
       service.iniciarIngresoOtros(otros_tipos, ingreso,"0");
    

       actos=service.consultaCarpetasByTipo(11, anio, mes);
       service.ingresoCateo(actos, ingreso, cateo, 99);// prueba anticipada
       
       otros_tipos=service.consultaCarpetasByTipo(18, anio, mes); 
       service.iniciarIngresoAccionParticular(otros_tipos, ingreso,"0");
       
      
      otros_tipos=service.consultaCarpetasByTipo(23, anio, mes); 
      service.iniciarIngresoOtrosuga12(otros_tipos, ingreso,"0");
       
      lista_ejecucion=service.consultaCarpetasByTipo(67, anio,mes);
      service.ingresoEjecucionIPreventivo(lista_ejecucion, ingreso);
      
      */
       
    /*  List<TCarpeta> lista=service.consultaCarpetas(2022, 1);
       service.actualizarImputados(lista, update);*/
       
     
      
      List<TCarpeta> lista_medidas=service.consultaCarpetasByTipo(52, anio, mes);
       medidas.actualizarMedidasCNPP(lista_medidas);
       
    List<TCarpeta> lista_medidas_acceso=service.consultaCarpetasByTipo(252, anio, mes);
       medidas.actualizarResolucionMedidasLAMVLV(lista_medidas_acceso);
     
      
     
     
      
     
     
      
      
       
    }

}
