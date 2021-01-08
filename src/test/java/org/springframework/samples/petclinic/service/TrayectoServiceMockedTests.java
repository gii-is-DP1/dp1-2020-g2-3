package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;

import aj.org.objectweb.asm.ClassTooLargeException;


@ExtendWith(MockitoExtension.class)
class TrayectoServiceMockedTests {

    @Mock
    private TrayectoRepository trayectoRepo;

    protected TrayectoService trayectoService;

    @BeforeEach
    void setup() {
    	trayectoService = new TrayectoService(trayectoRepo);

    }

  
    @Test
    @Transactional
    @DisplayName("calcular y asignar los diferentes trayectos de una ruta sin paradas intermedias cuyo origen y destino ES DIFERENTE a Zahinos")
    void calcularTrayectosRutaSinParadasTest1() throws DuplicatedParadaException {
   
    		//ARRANGE
    		when(trayectoService.findByOrigenAndDestino("Zahinos", "Badajoz")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Badajoz",72.3,1.15));
    		when(trayectoService.findByOrigenAndDestino("Badajoz", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Badajoz","Jerez de los Caballeros",73.7,1.03));
    		when(trayectoService.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
    		
        	Ruta rutaFormulario=nuevaRutaFormulario("Badajoz","Jerez de los Caballeros",new ArrayList<String>());
        	
        	//ACT
    		Ruta rutaConTrayectosCalculados=trayectoService.calcularYAsignarTrayectos(rutaFormulario);
    	/*	Trayectos que se tienen que crear:
    	 
    		Zahinos --> Badajoz ;;;;  Badajoz ---> Jerez de los Caballeros ;;;;  Jerez de los Caballeros --> >Zahinos */
    	
    		
    		//ASSERT
    
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().size(),3);
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getOrigen(),"Zahinos");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getDestino(),"Badajoz");
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getOrigen(),"Badajoz");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getDestino(),"Jerez de los Caballeros");
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(2).getOrigen(),"Jerez de los Caballeros");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(2).getDestino(),"Zahinos");

    
    	
    }
    
    @Test
    @Transactional
    @DisplayName("calcular y asignar los diferentes trayectos de una ruta sin paradas intermedias CUYO ORIGEN ES ZAHINOS")
    void calcularTrayectosRutaSinParadasTest2() throws DuplicatedParadaException{
    			
		
    	
    		//ARRANGE
    		when(trayectoService.findByOrigenAndDestino("Zahinos", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28));
    		when(trayectoService.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
    		
        	Ruta rutaFormulario=nuevaRutaFormulario("Zahinos","Jerez de los Caballeros",new ArrayList<String>());
        	

        	//ACT
    		Ruta rutaConTrayectosCalculados=trayectoService.calcularYAsignarTrayectos(rutaFormulario);
    		
    	/*	Trayectos que se tienen que crear:
    	 
    		Zahinos --> Jerez de los Caballeros ;;;;  Jerez de los Caballeros ---> Zahinos  */
    	
    		
    		//ASSERT
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().size(),2);
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getOrigen(),"Zahinos");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getDestino(),"Jerez de los Caballeros");
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getOrigen(),"Jerez de los Caballeros");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getDestino(),"Zahinos");

    	
    }
    
    @Test
    @Transactional
    @DisplayName("calcular y asignar los diferentes trayectos de una ruta sin paradas intermedias CUYO DESTINO ES ZAHINOS")
    void calcularTrayectosRutaSinParadasTest3() throws DuplicatedParadaException {
    			
	
    		//ARRANGE
    		when(trayectoService.findByOrigenAndDestino("Zahinos", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28));
    		when(trayectoService.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
        	
    		Ruta rutaFormulario=nuevaRutaFormulario("Jerez de los Caballeros","Zahinos",new ArrayList<String>());
        	

        	//ACT
    		Ruta rutaConTrayectosCalculados=trayectoService.calcularYAsignarTrayectos(rutaFormulario);
    		
    	/*	Trayectos que se tienen que crear:
    	 
    		Zahinos --> Jerez de los Caballeros ;;;;  Jerez de los Caballeros ---> Zahinos  */
    	
    		
    		//ASSERT
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().size(),2);
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getOrigen(),"Zahinos");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getDestino(),"Jerez de los Caballeros");
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getOrigen(),"Jerez de los Caballeros");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getDestino(),"Zahinos");

    	
    }
    
    @Test
    @Transactional
    @DisplayName("calcular los kilómetros totales de una ruta, incluyendo el número de horas QUE ESTÁ EL CLIENTE EN EL AUTOMÓVIL (no en toda la ruta...)")
    void calcularKmYHoras() throws DuplicatedParadaException {
    		
    		//ARRANGE
    		when(trayectoService.findByOrigenAndDestino("Zahinos", "Badajoz")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Badajoz",72.3,1.15));
    		when(trayectoService.findByOrigenAndDestino("Badajoz", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Badajoz","Jerez de los Caballeros",73.7,1.03));
    		when(trayectoService.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
			double numKmTotal= 72.3+73.7+19.1;
    		Double numKmTotalAproximado=0.0 + Math.round(numKmTotal*100)/100;
    		//¡Las horas estimadas del cliente no tendrán en cuenta los trayectos que realice únicamente el taxista!
    		Double horasEstimadasCliente=1.03; 
        	Ruta rutaFormulario=nuevaRutaFormulario("Badajoz","Jerez de los Caballeros",new ArrayList<String>());
        	
        	//ACT
    		Ruta rutaConTrayectosCalculados=trayectoService.calcularYAsignarTrayectos(rutaFormulario);
    		

    		//ASSERT
    		assertEquals(rutaConTrayectosCalculados.getNumKmTotales(),numKmTotalAproximado);
    		assertEquals(rutaConTrayectosCalculados.getHorasEstimadasCliente(),horasEstimadasCliente);
      
    }
    
    @Test
    @Transactional
    @DisplayName("Origen y destino del viaje iguales")
    public void origenIgualDestinoTest() {
    	//ARRANGE
    	Ruta rutaFormulario=nuevaRutaFormulario("Badajoz","Badajoz",new ArrayList<String>());
    	// ACT & ASSERT
       	assertThrows(DuplicatedParadaException.class,()->trayectoService.calcularYAsignarTrayectos(rutaFormulario));

    }
    
  
    
    
    
    
    /*
    El siguiente método crea una ruta con los datos dados como parámetro, 
    en el formato en el que vendría desde el formulario:
  	
  	La ruta tendrá como List<Trayecto> solo los trayectos intermedios
    sin tener en cuenta origen-->parada1, tampoco tiene en cuenta que si origen!=Zahinos
    el primer trayecto debería ser Zahinos --> origen
    
    Además, cada trayecto intermedio será trayecto1= {origen=parada1, destino=null}
    
    Con este formato se pasa la ruta como parámetro al método calcularYAsignarTrayectos y por ello lo tenemos 
    que simular para testearlo
   
    */
    static Ruta nuevaRutaFormulario(String origenCliente, String destinoCliente,List<String> paradasIntermedias) {
    	Ruta nuevaRuta= new Ruta();
 
    	List<Trayecto> trayectosRuta= new ArrayList<Trayecto>();
    	
    	nuevaRuta.setOrigenCliente(origenCliente);
    	nuevaRuta.setDestinoCliente(destinoCliente);
    	if(paradasIntermedias.size()>0) {
    		for (String parada: paradasIntermedias) {
        		Trayecto trayecto= new Trayecto();
        		trayecto.setOrigen(parada);
        		trayectosRuta.add(trayecto);
        	}
    	}
    	
    	nuevaRuta.setTrayectos(trayectosRuta);
    	return nuevaRuta;
    }
   
    /*
    static Stream<Ruta> diferentesRutasSinParadas1() {
    	
    	Ruta ruta1=nuevaRutaFormulario("Badajoz","Jerez de los Caballeros",new ArrayList<String>());
    	Ruta ruta2=nuevaRutaFormulario("Jerez de los Caballeros","Oliva de la Frontera",new ArrayList<String>());
    	Ruta ruta3=nuevaRutaFormulario("Oliva de la Frontera","Badajoz",new ArrayList<String>());
    
        return Stream.of(ruta1,ruta2,ruta3);
    }
    */

}
