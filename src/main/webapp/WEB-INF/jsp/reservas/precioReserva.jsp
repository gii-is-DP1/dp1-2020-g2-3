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
              <input type="hidden" name="numCiudadesIntermedias" id="numCiudadesIntermedias" value="${numCiudadesIntermedias}"/>

       <p> 
       <input type="hidden" name="ruta.origenCliente" id="ruta.origenCliente" value="${reserva.ruta.origenCliente}"/>
       <p> ${reserva.ruta.origenCliente} -->  </p>
     <c:choose>
       <c:when test = "${numCiudadesIntermedias>0}">
       <c:forEach var="i" begin="0" end="${finBucle}" step="1" varStatus ="status">
       <p> </p>
       		<input type="hidden" name="ruta.trayectos[${i}].origen" id="ruta.trayectos[${i}].origen" value="${reserva.ruta.trayectos[i].origen}" />
           	<p> ${reserva.ruta.trayectos[i].origen} -->  </p>
           	</c:forEach>
       </c:when>
     </c:choose>
      <input type="hidden" name="ruta.destinoCliente" id="ruta.destinoCliente" value="${reserva.ruta.destinoCliente}"/>
       
       <p> ${reserva.ruta.destinoCliente} </p>
       
       <em>  Nº Kilómetros totales: ${reserva.numKmTotales} <em> 
       <h2>Fecha de Salida</h2>
       <p> Fecha: <fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaSalida}" /> </p>
      <input type="hidden" name="fechaSalida"  id="fechaSalida" value="<fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaSalida}" />"/>     
       
		<p> Hora <fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaSalida}" /> </p>  
	   <input type="hidden"  name="horaSalida" id="horaSalida" value="<fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaSalida}" />"/>
	
		<h2>Fecha de Llegada</h2>

		<p> Fecha: <fmt:formatDate pattern = "yyyy-MM-dd" value = "${reserva.fechaLlegada}" /> </p>
		<p> Hora <fmt:formatDate type = "time" pattern="HH:mm" value = "${reserva.horaLlegada}" /> </p>  
	
      <p>  Duración del viaje:  ${horasRutaCliente} horas y ${minutosRutaCliente} minutos </p>
      <h2>Precio Total: </h2>  ${reserva.precioTotal} euros 
        
        <br><br>
        <p> Plazas Ocupadas: ${reserva.plazas_Ocupadas} </p>
         <input type="hidden" name="plazas_Ocupadas" id="plazas_Ocupadas" value="${reserva.plazas_Ocupadas}"/>
        <p> Descripción del equipaje: ${reserva.descripcionEquipaje} </p>
        <input type="hidden" name="descripcionEquipaje" id="descripcionEquipaje" value="${reserva.descripcionEquipaje}"/>

     	<button class="btn btn-default" type="submit" name="action" value="atras">Atrás</button> <span> &nbsp;&nbsp;		<button class="btn btn-default" type="submit" name="action" value="confirmarReserva">Reservar viaje</button> </span>	 
     	 
    	
    </form:form>    
    
</petclinic:layout>