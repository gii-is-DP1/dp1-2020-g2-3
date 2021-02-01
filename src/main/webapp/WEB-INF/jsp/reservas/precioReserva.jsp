<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="reservas">
    <form:form modelAttribute="reserva" class="form-horizontal" id="add-reserva-form" action="/reservas/redirigirPrecioReserva">
        
       
        
       <h2>Ruta establecida</h2>
      
              <input type="hidden" name="numCiudadesIntermedias" id="numCiudadesIntermedias" value="${numCiudadesIntermedias}"/>
	      <jsp:include page="/WEB-INF/jsp/reservas/mostrarRuta.jsp"/>
	
      <br>

       <em>  Nº Kilómetros totales: ${reserva.numKmTotales} </em>   <br> </br>
        <p style="border:3px; border-style:solid; border-color:#000000; background-color:#FFF699">
       *Para el cálculo de kilómetros totales se tiene en cuenta el <span style="color:green">  trayecto que realiza el taxista para llegar al origen </span>,
       así como el de <span style="color:green"> vuelta hasta su localidad (Zahínos) </span> <br>
       **Los trayectos en verde no le corresponden al cliente <br> </p>
       <br>
           
       <h2>Fecha de Salida</h2>
       <p> Fecha: <fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaSalida}" /> </p>
      <input type="hidden" name="fechaSalida"  id="fechaSalida" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaSalida}" />"/>     
       
		<p> Hora <fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaSalida}" /> </p>  
	   <input type="hidden"  name="horaSalida" id="horaSalida" value="<fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaSalida}" />"/>
	
		<h2>Fecha Estimada de Llegada</h2>

		<p> Fecha: <fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaLlegada}" /> </p>
		<p> Hora <fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaLlegada}" /> </p>  
      <p>  Duración del viaje:  ${horasRutaCliente} horas y ${minutosRutaCliente} minutos </p> <br>
      
      <p style="border:3px; border-style:solid; border-color:#000000; background-color:#FFF699">
      ***La duración del viaje, así como la fecha estimada de llegada, corresponde a la ruta realizada por el cliente (Trayectos en negro)
       </p>
      
      
      
        <h2> Detalles del viaje </h2>
        <p> Plazas Ocupadas: ${reserva.plazas_Ocupadas} </p>
         <input type="hidden" name="plazas_Ocupadas" id="plazas_Ocupadas" value="${reserva.plazas_Ocupadas}"/>
        <p> Descripción del equipaje: ${reserva.descripcionEquipaje} </p>
        <input type="hidden" name="descripcionEquipaje" id="descripcionEquipaje" value="${reserva.descripcionEquipaje}"/>

 		<h2>Precio Total: </h2> <span> ${reserva.precioTotal} euros</span>
		<br><br>
     	<button class="btn btn-default" type="submit" name="action" value="atras"><span class="glyphicon glyphicon-menu-left" aria-hidden="false"></span>Atrás</button> <span> &nbsp;&nbsp;		<button class="btn btn-default" type="submit" name="action" value="confirmarReserva">Reservar viaje</button> </span>	 
     	 
    	
    </form:form>    
    
</petclinic:layout>