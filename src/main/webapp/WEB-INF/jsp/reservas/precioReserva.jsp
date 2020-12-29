<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="reservas">
    <form:form modelAttribute="reserva" class="form-horizontal" id="add-reserva-form" action="/new">
        
        <!-- EN DESARROLLO -->
        
       <h2>Ruta establecida</h2>
       <p> 
       
       		<c:forEach items="${ruta.trayectos}" var="trayecto">
       		${trayecto.origen} --> ${trayecto.destino} <br> </br>
           	</c:forEach>
           	
       </p>
       <br>
       <em>  Nº Kilómetros totales: ${ruta.numKmTotales} <em>
       <br>
        <h2>Precio</h2>
        ${reserva.precioTotal}
    	Precio/km= 0.41 euros
        
        <br><br>
        
         <h2>Duración del viaje</h2>
        ${ruta.horasEstimadasCliente} horas
        
        <br><br>
    	   
    	   
     	<button class="btn btn-default"  name="action"> <- Atrás</button> 		 
    	
		<button class="btn btn-default" type="submit" name="action">Reservar viaje</button> 		 
    </form:form>    
    
</petclinic:layout>