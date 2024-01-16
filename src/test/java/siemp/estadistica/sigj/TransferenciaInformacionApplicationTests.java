package siemp.estadistica.sigj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import siemp.estadistica.sigj.service.ConsultaDatosService;

@SpringBootTest
class TransferenciaInformacionApplicationTests {

	@Autowired
	public ConsultaDatosService service;
	@Test
	void contextLoads() {
		service.consultaCarpetas();
	}

}
