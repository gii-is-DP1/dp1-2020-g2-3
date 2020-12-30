<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="reservas">
    <form:form modelAttribute="reserva" class="form-horizontal" id="add-reserva-form" action="/reservas/redirigir">
        
        <!-- EN DESARROLLO -->
        
       <h2>Ruta establecida</h2>
       <p> 
       <input type="hidden" name="reserva" id="reserva" value="${reserva}"/>
       		<c:forEach items="${reserva.ruta.trayectos}" var="trayecto">
       		${trayecto.origen} --> ${trayecto.destino} <br> </br>
           	</c:forEach>
       </p>
       <em>  Nº Kilómetros totales: ${reserva.numKmTotales} <em> 
       <h2>Fecha de Salida</h2>
       <p> Fecha: <fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaSalida}" /> </p>
		<p> Hora <fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaSalida}" /> </p>  
	
		<h2>Fecha de Llegada</h2>

		<p> Fecha: <fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaLlegada}" /> </p>
		<p> Hora <fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaLlegada}" /> </p>  
	
      <p>  Duración del viaje:  ${horasRutaCliente} horas y ${minutosRutaCliente} minutos </p>
      <h2>Precio Total: </h2>  ${reserva.precioTotal} euros 
        
        <br><br>   
     	<button class="btn btn-default" type="submit" name="action" value="atras"> <- Atrás</button> 		 
    	
		<button class="btn btn-default" type="submit" name="action" value="confirmarReserva">Reservar viaje</button> 		 
    </form:form>    
    
</petclinic:layout>